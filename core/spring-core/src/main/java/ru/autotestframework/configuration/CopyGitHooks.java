package ru.autotestframework.configuration;

import static ru.autotestframework.Constants.DISABLE_HOOKS_COPY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import ru.autotestframework.core.exception.ConfigurationException;

/**
 * The class provides copying of git hooks from the resources/git-hooks directory to the directory.git/hooks at the root of the project
 * The basic set of hooks is located in the core_base module and copied from it
 * Hooks should provide control over the naming of branches and commits, check compilation, and run tests
 * on each commit. This will avoid problems with uncompiled code in the repository.
 * and will ensure the traceability of commits.
 * The backend_core module uses a scheme to start copying hooks when executing a build.
 * In projects that use backend_core, the copy will be started at local startup.
 * autotests. This will ensure that the hooks are updated more or less regularly to the latest version.
 * If you want to use your own hooks in the project, then you need to create your own resources/git-hooks directory.
 * and place your files with hooks in it.
 * To remove hooks from .git/hooks, use the following steps:
 * For bdd_backend_framework, go to build.gradle in the hookCopy task, set DISABLE_HOOKS_COPY to true and manually
 * remove hooks from .git/hooks (which do not end in .sample)
 * For projects, you need to specify a parameter in framework.properties
 * framework.disable.hooks.copy=true
 * And run any autotest
 * I strongly recommend disabling hooks only if they cause problems.
 */
@Slf4j
public class CopyGitHooks {
    private static final String GIT_HOOKS_MISSING =
            "Git hooks enabled, add dependency providing hooks or create appropriate file(-s) in resources";
    private final boolean removeGitHooks;
    private final List<Resource> originalHooksSource = new ArrayList<>();
    private final List<Path> activeHooks = new ArrayList<>();
    private Path hookPath;

    /**
     * Instantiates a new Copy git hooks.
     *
     * @param removeGitHooks the remove git hooks
     */
    public CopyGitHooks(final String removeGitHooks) {
        this.removeGitHooks = "true".equals(removeGitHooks);
    }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(final String[] args) {
        final String argument = args.length > 0 ? args[0] : "false";
        new CopyGitHooks(argument).copyGitHooks();
    }

    /**
     * Copy git hooks.
     */
    @SneakyThrows
    public void copyGitHooks() {
        if ("true".equals(System.getenv(DISABLE_HOOKS_COPY))) {
            return;
        }
        // Находим корневую директорию проекта в которой находится .git
        final Optional<Path> optionalRootProject = getProjectRoot(Paths.get(System.getProperty("user.dir")));
        if (optionalRootProject.isEmpty()) {
            log.info("Root of project with .git folder wasn't found");
            return;
        }
        // получаем список ресурсов с хуками из core_base
        collectOriginalHooksSource();

        hookPath = optionalRootProject.get().resolve(".git").resolve("hooks");
        // При выполнении в ранере папка с хуками не создаётся
        if (!Files.exists(hookPath)) {
            return;
        }
        // список действующих локально хуков
        try (Stream<Path> paths = Files.list(hookPath)) {
            paths.filter(h -> !h.toString().endsWith(".sample")).forEach(activeHooks::add);
        }

        // удаляем хуки и выходим если включена настройка framework.remove.git.hooks=true
        if (removeGitHooks) {
            activeHooks.forEach(this::removeHook);
            return;
        }

        // удаляем хуки, которых нет в originalHooks
        removeUnusedHooks();

        // копируем хуки из originalHooks в локальную директорию
        originalHooksSource.forEach(this::copyHook);
    }

    // Метод выполняет запись хука из core_base в .git/hooks
    private void copyHook(final Resource resource) {
        if (Objects.isNull(resource.getFilename())) {
            return;
        }
        final var ohName = Paths.get(resource.getFilename());
        final List<String> originHookContent = readResource(resource);

        for (final Path hook : activeHooks) {
            if (hook.getFileName().equals(ohName)) {
                // если контент хука не отличается - пропускаем
                final List<String> activeHookContent = readFile(hook);
                if (compareData(originHookContent, activeHookContent)) {
                    return;
                }
            }
        }
        final var target = hookPath.resolve(ohName);
        try {
            Files.write(target, originHookContent);
        } catch (IOException e) {
            log.info("Hook file wasn't copied", e);
        }
    }

    // Метод сравнивает содержимое двух файлов
    private boolean compareData(final List<String> expected, final List<String> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }
        for (var i = 0; i < actual.size(); i++) {
            if (!expected.get(i).equals(actual.get(i))) {
                return false;
            }
        }
        return true;
    }

    // Метод вычитывает содержимое хука по его пути
    private List<String> readFile(final Path path) {
        try (Stream<String> paths = Files.lines(path)) {
            return paths.collect(Collectors.toList());
        } catch (final IOException e) {
            throw new ConfigurationException("The file '{}' can't be read", e, path.toString());
        }
    }

    // Метод вычитывает содержимое хука из core_base
    private List<String> readResource(final Resource resource) {
        try (var isr = new InputStreamReader(resource.getInputStream());
                var br = new BufferedReader(isr)) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new ConfigurationException("The file '{}' can't be read", e, resource.getFilename());
        }
    }

    // Метод удаляет из .git/hooks все кастомные хуки, которые не используются в core_base
    private void removeUnusedHooks() {
        final List<Path> originalHookNames = originalHooksSource.stream()
                .map(Resource::getFilename)
                .map(Paths::get)
                .map(Path::getFileName)
                .collect(Collectors.toList());

        new ArrayList<>(activeHooks).forEach(ah -> {
            if (!originalHookNames.contains(ah.getFileName())) {
                removeHook(ah);
                activeHooks.remove(ah);
            }
        });
    }

    // Метод удаляет файл
    private void removeHook(final Path path) {
        try {
            Files.delete(path);
        } catch (final IOException e) {
            log.info("Hook file wasn't deleted", e);
        }
    }

    // Метод выполняет поиск корневой директории проекта, которая содержит .git
    private Optional<Path> getProjectRoot(final Path startPath) {
        return Objects.isNull(startPath)
                ? Optional.empty()
                : Files.exists(startPath.resolve(".git"))
                        ? Optional.of(startPath)
                        : getProjectRoot(startPath.getParent());
    }

    @SneakyThrows
    // Метод выполняет поиск хуков в директории git-hooks/
    private void collectOriginalHooksSource() {
        try {
            final var resolver = new PathMatchingResourcePatternResolver();
            final Resource[] resources = resolver.getResources("git-hooks/*");
            originalHooksSource.addAll(Arrays.asList(resources));
        } catch (IOException e) {
            log.warn(GIT_HOOKS_MISSING, e);
        }
    }
}

(function (xpath) {
    let elements = document.evaluate(
        xpath.toString(), document, null, XPathResult.ANY_TYPE, null);
    let node = elements.iterateNext();
    let values = [];

    while (node) {
        // Получаем все текстовые узлы
        let textNodes = [];
        let walk = document.createTreeWalker(
            node,
            NodeFilter.SHOW_TEXT,
            {
                acceptNode: function(node) {
                    // Игнорируем скрытые элементы и пустые текстовые узлы
                    if (node.parentElement.offsetParent === null ||
                        !node.nodeValue.trim()) {
                        return NodeFilter.FILTER_REJECT;
                    }
                    return NodeFilter.FILTER_ACCEPT;
                }
            }
        );

        let textNode;
        while (textNode = walk.nextNode()) {
            textNodes.push(textNode.nodeValue.trim());
        }

        // Убираем дубликаты, но сохраняем порядок
        let uniqueTexts = [];
        let seen = new Set();
        for (let text of textNodes) {
            if (!seen.has(text)) {
                seen.add(text);
                uniqueTexts.push(text);
            }
        }

        values.push(uniqueTexts.join(' '));
        node = elements.iterateNext();
    };

    return values;
})(arguments[0]);
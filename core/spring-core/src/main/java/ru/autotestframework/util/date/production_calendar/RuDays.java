package ru.autotestframework.util.date.production_calendar;

/**
 * Production calendar for the Russian Federation 2013-2027.
 */
public class RuDays extends DefaultDays {

    private static final String NEW_YEAR = "Новогодние каникулы (в ред. Федерального закона от 23.04.2012 № 35-ФЗ)";
    private static final String CHRISTMAS = "Рождество Христово";
    private static final String MENDAY = "День защитника Отечества";
    private static final String WOMENDAY = "Международный женский день";
    private static final String MAYDAY = "Праздник Весны и Труда";
    private static final String VICTORYDAY = "День Победы";
    private static final String RUSSIADAY = "День России";
    private static final String PEOPLEDAY = "День народного единства";

    @Override
    public void init() {

        // 2013 Holidays list
        add("2013-22-02", DayType.SHORTDAY);
        add("2013-07-03", DayType.SHORTDAY);
        add("2013-10-03", DayType.HOLIDAY);
        add("2013-30-04", DayType.SHORTDAY);
        add("2013-02-05", DayType.HOLIDAY);
        add("2013-03-05", DayType.HOLIDAY);
        add("2013-08-05", DayType.SHORTDAY);
        add("2013-10-05", DayType.HOLIDAY);
        add("2013-11-06", DayType.SHORTDAY);
        add("2013-31-12", DayType.SHORTDAY);

        // 2014 Holidays list
        add("2014-24-02", DayType.SHORTDAY);
        add("2014-07-03", DayType.SHORTDAY);
        add("2014-10-03", DayType.HOLIDAY);
        add("2014-30-04", DayType.SHORTDAY);
        add("2014-02-05", DayType.HOLIDAY);
        add("2014-08-05", DayType.SHORTDAY);
        add("2014-11-06", DayType.SHORTDAY);
        add("2014-13-06", DayType.HOLIDAY);
        add("2014-03-11", DayType.HOLIDAY);
        add("2014-31-12", DayType.SHORTDAY);

        // 2015 Holidays list
        add("2015-09-01", DayType.HOLIDAY);
        add("2015-09-03", DayType.HOLIDAY);
        add("2015-30-04", DayType.SHORTDAY);
        add("2015-04-05", DayType.HOLIDAY);
        add("2015-08-05", DayType.SHORTDAY);
        add("2015-11-05", DayType.HOLIDAY);
        add("2015-11-06", DayType.SHORTDAY);
        add("2015-03-11", DayType.SHORTDAY);
        add("2015-31-12", DayType.SHORTDAY);

        // 2016 Holidays list
        add("2016-20-02", DayType.SHORTDAY);
        add("2016-22-02", DayType.HOLIDAY);
        add("2016-07-03", DayType.HOLIDAY);
        add("2016-02-05", DayType.HOLIDAY);
        add("2016-03-05", DayType.HOLIDAY);
        add("2016-13-06", DayType.HOLIDAY);
        add("2016-03-11", DayType.SHORTDAY);

        // 2017 Holidays list
        add("2017-22-02", DayType.SHORTDAY);
        add("2017-24-02", DayType.HOLIDAY);
        add("2017-07-03", DayType.SHORTDAY);
        add("2017-08-05", DayType.HOLIDAY);
        add("2017-03-11", DayType.SHORTDAY);
        add("2017-06-11", DayType.HOLIDAY);

        // 2018 Holidays list
        add("2018-22-02", DayType.SHORTDAY);
        add("2018-07-03", DayType.SHORTDAY);
        add("2018-09-03", DayType.HOLIDAY);
        add("2018-28-04", DayType.SHORTDAY);
        add("2018-30-04", DayType.HOLIDAY);
        add("2018-02-05", DayType.HOLIDAY);
        add("2018-08-05", DayType.SHORTDAY);
        add("2018-09-06", DayType.SHORTDAY);
        add("2018-11-06", DayType.HOLIDAY);
        add("2018-05-11", DayType.HOLIDAY);
        add("2018-29-12", DayType.SHORTDAY);
        add("2018-31-12", DayType.HOLIDAY);

        // 2019 Holidays list
        add("2019-22-02", DayType.SHORTDAY);
        add("2019-07-03", DayType.SHORTDAY);
        add("2019-30-04", DayType.SHORTDAY);
        add("2019-02-05", DayType.HOLIDAY);
        add("2019-03-05", DayType.HOLIDAY);
        add("2019-08-05", DayType.SHORTDAY);
        add("2019-10-05", DayType.HOLIDAY);
        add("2019-11-06", DayType.SHORTDAY);
        add("2019-31-12", DayType.SHORTDAY);

        // 2020 Holidays list
        add("2020-24-02", DayType.HOLIDAY);
        add("2020-09-03", DayType.HOLIDAY);
        add("2020-04-05", DayType.HOLIDAY);
        add("2020-05-05", DayType.HOLIDAY);
        add("2020-11-05", DayType.HOLIDAY);
        add("2020-11-06", DayType.SHORTDAY);
        add("2020-03-11", DayType.SHORTDAY);
        add("2020-31-12", DayType.SHORTDAY);

        // 2021 Holidays list
        add("2021-20-02", DayType.WORKDAY);
        add("2021-22-02", DayType.HOLIDAY);
        add("2021-07-03", DayType.HOLIDAY);
        add("2021-30-04", DayType.SHORTDAY);
        add("2021-02-05", DayType.HOLIDAY);
        add("2021-03-05", DayType.HOLIDAY);
        add("2021-10-05", DayType.HOLIDAY);
        add("2021-11-06", DayType.SHORTDAY);
        add("2021-14-06", DayType.HOLIDAY);
        add("2021-05-11", DayType.HOLIDAY);
        add("2021-31-12", DayType.HOLIDAY);

        // 2022 Holidays list
        add("2022-22-02", DayType.SHORTDAY);
        add("2022-05-03", DayType.WORKDAY);
        add("2022-07-03", DayType.HOLIDAY);
        add("2022-02-05", DayType.HOLIDAY);
        add("2022-03-05", DayType.HOLIDAY);
        add("2022-10-05", DayType.HOLIDAY);
        add("2022-13-06", DayType.HOLIDAY);
        add("2022-03-11", DayType.SHORTDAY);

        // 2023 Holidays list
        add("2023-22-02", DayType.SHORTDAY);
        add("2023-24-02", DayType.HOLIDAY);
        add("2023-07-03", DayType.SHORTDAY);
        add("2023-08-05", DayType.HOLIDAY);
        add("2023-03-11", DayType.SHORTDAY);
        add("2023-06-11", DayType.HOLIDAY);

        // 2024 Holidays list
        add("2024-22-02", DayType.SHORTDAY);
        add("2024-07-03", DayType.SHORTDAY);
        add("2024-09-03", DayType.HOLIDAY);
        add("2024-27-04", DayType.WORKDAY);
        add("2024-29-04", DayType.HOLIDAY);
        add("2024-30-04", DayType.HOLIDAY);
        add("2024-08-05", DayType.SHORTDAY);
        add("2024-10-05", DayType.HOLIDAY);
        add("2024-11-06", DayType.SHORTDAY);
        add("2024-02-11", DayType.WORKDAY);
        add("2024-28-12", DayType.WORKDAY);
        add("2024-30-12", DayType.HOLIDAY);
        add("2024-31-12", DayType.HOLIDAY);

        // 2025 Holidays list
        add("2025-07-03", DayType.SHORTDAY);
        add("2025-02-05", DayType.HOLIDAY);
        add("2025-08-05", DayType.HOLIDAY);
        add("2025-11-06", DayType.SHORTDAY);
        add("2025-13-06", DayType.HOLIDAY);
        add("2025-01-11", DayType.SHORTDAY);
        add("2025-03-11", DayType.HOLIDAY);
        add("2025-31-12", DayType.HOLIDAY);

        // 2026 Holidays list
        add("2026-09-03", DayType.HOLIDAY);
        add("2026-30-04", DayType.SHORTDAY);
        add("2026-04-05", DayType.HOLIDAY);
        add("2026-05-05", DayType.HOLIDAY);
        add("2026-08-05", DayType.SHORTDAY);
        add("2026-11-05", DayType.HOLIDAY);
        add("2026-11-06", DayType.SHORTDAY);
        add("2026-03-11", DayType.SHORTDAY);
        add("2026-31-12", DayType.SHORTDAY);

        // 2027 Holidays list
        add("2027-22-02", DayType.HOLIDAY);
        add("2027-27-02", DayType.WORKDAY);
        add("2027-30-04", DayType.SHORTDAY);
        add("2027-02-05", DayType.HOLIDAY);
        add("2027-03-05", DayType.HOLIDAY);
        add("2027-10-05", DayType.HOLIDAY);
        add("2027-11-06", DayType.SHORTDAY);
        add("2027-14-06", DayType.HOLIDAY);
        add("2027-03-11", DayType.SHORTDAY);
        add("2027-05-11", DayType.HOLIDAY);
        add("2027-31-12", DayType.SHORTDAY);
    }
}

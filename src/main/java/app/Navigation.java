package app;

/** Shared navigation keeps the same routes available at every screen size. */
public final class Navigation {
    private Navigation() { }

    public static String render(String currentPage) {
        String[][] links = {
            {"/", "Home"}, {"/mission.html", "Our mission"},
            {"/datasummary.html", "About the data"}, {"/page2A.html", "Weather stations"},
            {"/page2B.html", "Climate metrics"}, {"/page2C.html", "Data quality"},
            {"/page3A.html", "Station similarities"}, {"/page3B.html", "Metric similarities"},
            {"/page3C.html", "Cross-metric impact"}
        };
        StringBuilder html = new StringBuilder("""
            <a class='skip-link' href='#main-content'>Skip to content</a>
            <header class='navbar'>
                <a class='site-brand' href='/'>
                    <img class='logo' src='/home.png' alt='' width='56' height='56'>
                    <span>Australian Climate<br>Data Explorer</span>
                </a>
                <nav class='navbar-button' aria-label='Main navigation'>
                    <button class='menu-toggle' type='button' aria-expanded='false' aria-controls='navigation-links'>
                        Explore tools
                        <svg width='20' height='20' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' aria-hidden='true'>
                            <path d='M4 7h16M4 12h16M4 17h16'/>
                        </svg>
                    </button>
                    <ul id='navigation-links'>
            """);
        for (String[] link : links) {
            html.append("<li><a href='").append(link[0]).append("'");
            if (link[0].equals(currentPage)) html.append(" aria-current='page'");
            html.append(">").append(link[1]).append("</a></li>");
        }
        return html.append("</ul></nav></header><main id='main-content' tabindex='-1'>").toString();
    }
}

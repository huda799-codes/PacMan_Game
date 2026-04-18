package PacmanGame;

public class MapData {
    public static final int TILE_SIZE=30;

    private char[][] map={
            "WWWWWWWWWWWWWWWWWWWWWWWWWWWW".toCharArray(),
            "W............WW............W".toCharArray(),
            "W.WWWW.WWWWW.WW.WWWWW.WWWW.W".toCharArray(),
            "W.C....W..........W....C...W".toCharArray(),
            "W.WWWW.W.WWWWWW.W.W.WWWW.W.W".toCharArray(),
            "W......W....WW..W.W........W".toCharArray(),
            "W.WWWW.WW.W.WW.WW.W.WWWW.WWW".toCharArray(),
            "W......W..W....W..W........W".toCharArray(),
            "W.WWWW.W.WWWWWWWW.W.WWWW.W.W".toCharArray(),
            "W..C...W....P.....W...C....W".toCharArray(),
            "W.WWWW.W.WWWWWW.W.W.WWWW.W.W".toCharArray(),
            "W............WW............W".toCharArray(),
            "WWWWWWWWWWWWWWWWWWWWWWWWWWWW".toCharArray()
    };

    public char[][] getMap() {
        return map;
    }
}

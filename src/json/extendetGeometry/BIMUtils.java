package json.extendetGeometry;

import java.util.Collection;

/**
 * Created by boris on 01.02.17.
 */
public class BIMUtils {

    public static GeometryBIM size(BIMExt bim) {
        GeometryBIM g = new GeometryBIM();
        Collection<ZoneExt> zones = bim.getZones().values();

        for (ZoneExt zone : zones) {
            if (zone.isSafetyZone()) continue;
            for (int i = 0; i < zone.getXyz()[0].length; i++) {
                double x = zone.getXyz(0, i, 0);
                if (x <= g.getMinX()) g.setMinX(x);
                if (x >= g.getMaxX()) g.setMaxX(x);

                double y = zone.getXyz(0, i, 1);
                if (y <= g.getMinY()) g.setMinY(y);
                if (y >= g.getMaxY()) g.setMaxY(y);
            }
        }

        return g;
    }
}

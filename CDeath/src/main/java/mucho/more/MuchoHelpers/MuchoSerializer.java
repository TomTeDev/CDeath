package mucho.more.MuchoHelpers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class MuchoSerializer {
    private static String preciseLocSpacer = "prcs";
    private static String baseLocSpacer = "prcs";
    @CheckForNull
    public static String serializeLocation(Location loc, boolean precise){
        if(loc==null)return null;
        if(loc.getWorld()==null)return null;
        String worldName = loc.getWorld().getName();
        float yaw,pitch;
        int x,y,z;
        double xd,yd,zd;
        StringBuilder sb = new StringBuilder();
        if(precise){
            xd = loc.getX();
            yd = loc.getY();
            zd = loc.getZ();
            yaw = loc.getYaw();
            pitch = loc.getPitch();
            sb.append(worldName).append(preciseLocSpacer).append(xd).append(preciseLocSpacer).append(yd)
                    .append(preciseLocSpacer).append(zd).append(preciseLocSpacer).append(yaw).append(preciseLocSpacer).append(pitch);
        }else{
            x = loc.getBlockX();
            y = loc.getBlockY();
            z = loc.getBlockZ();
            sb.append(worldName).append(baseLocSpacer).append(x).append(baseLocSpacer).append(y)
                    .append(baseLocSpacer).append(z);
        }
        return sb.toString();
    }
    @CheckForNull
    public static Location deserializeLocation(String locString){
        if(locString==null)return null;
        if(locString.contains(baseLocSpacer)){
            String[] data = locString.split(baseLocSpacer);
            if(data.length!=4)return null;
            try {
                World w = Bukkit.getWorld(data[0]);
                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);
                int z = Integer.parseInt(data[3]);
                if(w==null){
                    MuchoDebuger.error("World is null. Deserialization of location error: "+locString);
                    return null;
                }
                return new Location(w,x,y,z);
            }catch (Exception e){
                MuchoDebuger.error("Error while deserializating location: "+locString);
            }
        }
        if(locString.contains(preciseLocSpacer)){
            String[] data = locString.split(baseLocSpacer);
            if(data.length!=6)return null;
            try {
                World w = Bukkit.getWorld(data[0]);
                double x = Double.parseDouble(data[1]);
                double y = Double.parseDouble(data[2]);
                double z = Double.parseDouble(data[3]);
                float yaw = Float.parseFloat(data[4]);
                float pitch = Float.parseFloat(data[5]);
                if(w==null){
                    MuchoDebuger.error("World is null. Deserialization of location error: "+locString);
                    return null;
                }
                return new Location(w,x,y,z,yaw,pitch);
            }catch (Exception e){
                MuchoDebuger.error("Error while deserializating location: "+locString);
            }
        }

        return null;
    }
}

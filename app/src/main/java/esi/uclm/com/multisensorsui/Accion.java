package esi.uclm.com.multisensorsui;

/**
 * Created by Javier on 09/11/2014.
 */
public class Accion {

    private String nombre;
    private float minY;
    private float maxY;
    private float minZ;
    private float maxZ;
    private String accionSel;

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMinZ() {
        return minZ;
    }

    public void setMinZ(float minZ) {
        this.minZ = minZ;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(float maxZ) { this.maxZ = maxZ; }

    public String getAccionSel() { return accionSel; }

    public void setAccionSel(String accionSel) { this.accionSel = accionSel; }

    public Accion (String nombre, float minY, float maxY, float minZ, float maxZ, String accionSel){
        this.nombre = nombre;

        this.minY = minY;
        this.maxY = maxY;

        this.minZ = minZ;
        this.maxZ = maxZ;

        this.accionSel = accionSel;
    }

    public Accion() {

    }

    @Override
    public String toString() {
        return "Accion{" +
                "nombre='" + nombre + '\'' +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                ", actionSel=" + accionSel +
                '}';
    }
}

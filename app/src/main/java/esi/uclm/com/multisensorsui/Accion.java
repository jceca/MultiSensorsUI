package esi.uclm.com.multisensorsui;

/**
 * Created by Javier on 09/11/2014.
 */
public class Accion {

    private int tipo;
    private String nombre;
    private float minY;
    private float maxY;
    private float minZ;
    private float maxZ;
    private int nShakes;
    private String accionSel;
    private long time;

    public int getTipo(){
        return  tipo;
    }

    public void setTipo(int tipo){ this.tipo = tipo; }

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

    public int getNShakes(){
        return nShakes;
    }

    public void setNShakes(int nShakes){ this.nShakes = nShakes; }

    public String getAccionSel() { return accionSel; }

    public void setAccionSel(String accionSel) { this.accionSel = accionSel; }

    public long getTime(){ return time; }

    public void setTime(long time){ this.time = time; }

    public Accion (int tipo, String nombre, float minY, float maxY, float minZ, float maxZ, int nShakes, String accionSel){
        this.tipo = tipo;

        this.nombre = nombre;

        this.minY = minY;
        this.maxY = maxY;

        this.minZ = minZ;
        this.maxZ = maxZ;

        this.nShakes = nShakes;

        this.accionSel = accionSel;
        time = System.currentTimeMillis();
    }

    public Accion() {

    }

    @Override
    public String toString() {
        return "Accion{" +
                " tipo=" + tipo + '\'' +
                "nombre='" + nombre +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                ", nShakes=" + nShakes +
                ", accionSel='" + accionSel + '\'' +
                ", time=" + time +
                '}';
    }
}

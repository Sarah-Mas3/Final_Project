public class Level {
    private int size;
    private int trapCount;
    private int firstAid;

   public Level(int size, int trapCount, int firstAid ){
       this.size = size;
       this.firstAid = firstAid;
       this.trapCount = trapCount;
   }


    public int getSize() {
        return size;
    }

    public int getTrapCount() {
        return trapCount;
    }

    public int getFirstAid() {
        return firstAid;
    }

}
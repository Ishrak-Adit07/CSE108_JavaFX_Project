public class Count {
    String name;
    int count;

    public Count(){
        name=new String();
        count=0;
    }
    public Count(String line){
        name=line;
        count=1;
    }
    public void setName(String line) {
        this.name = line;
    }
    public void print(){
        System.out.println(name+"    Movies: "+count);
    }
}

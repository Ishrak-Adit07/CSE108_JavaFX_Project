class Movie{
    private String title;
    private int release_year;
    private String genre1, genre2, genre3;
    private int running_time;
    private String producer;
    private int budget;
    private int revenue;
    private int profit;

    public Movie(String line){
        String [] data = line.split(",");
        this.title=data[0];
        this.release_year = Integer.parseInt(data[1]);
        this.genre1=data[2];
        this.genre2=data[3];
        this.genre3=data[4];
        this.running_time=Integer.parseInt(data[5]);
        this.producer=data[6];
        this.budget=Integer.parseInt(data[7]);
        this.revenue=Integer.parseInt(data[8]);
        this.profit=this.revenue-this.budget;
    }

    public void print(){
        System.out.println(title+","+release_year+","+genre1+","+genre2+","+genre3+","+running_time+","+producer+","+budget+","+revenue);
    }

    public String getTitle(){
        return this.title;
    }
    public int getRelease_year(){
        return this.release_year;
    }
    public String getGenre1(){
        return this.genre1;
    }
    public String getGenre2(){
        return this.genre2;
    }
    public String getGenre3(){
        return this.genre3;
    }
    public String getProducer(){
        return this.producer;
    }
    public int getRunning_time(){
        return this.running_time;
    }
    public int getBudget(){
        return this.budget;
    }
    public int getRevenue(){
        return this.revenue;
    }
    public int getProfit(){
        return this.profit;
    }
}

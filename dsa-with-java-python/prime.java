class prime{
    public static void main(String[] args){
        for(int i=2;i<100;i++){
            int count = 0;
            for(int j=1;j<=i;j++){
                if(i%j==0){
                    count++;
                }
            }
            if(count==2){
                System.out.println("prime");
                System.out.println(i);
            }
            else{
                System.out.println("not prime");
                System.out.println(i);
            }
        }
    }
}
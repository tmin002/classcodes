#include <stdio.h>
#include <conio.h>
 
int main(){
    int testCase,jobs;
    int i,j,k,a,b,tmp;
    int time,fine,jobSerial[10000];
    double fineByTime[10000];
    double temp;
    freopen("input.txt","r",stdin);
    scanf("%d",&testCase);
    for(i=1;i<=testCase;i++){
       if(i!=1)printf("\n");
      scanf("%d",&jobs);
      for(j=0;j<jobs;j++){
       jobSerial[j] = j;
       scanf("%d %d",&time,&fine);
       fineByTime[j] = (double)((double)fine/(double)time);
      }
        
    for(a=1;a<jobs;++a){
     for(b=jobs-1;b>=a;--b){
      if(fineByTime[b-1]<fineByTime[b]){
       tmp = jobSerial[b-1];
       temp=fineByTime[b-1];
        
       jobSerial[b-1] = jobSerial[b];
       fineByTime[b-1]= fineByTime[b];
        
       jobSerial[b] = tmp;
       fineByTime[b]=temp;
        
      }
     }
    }
    for(a=0;a<jobs;a++){
       printf("%d",jobSerial[a]+1); 
       if(a!=jobs-1)printf(" ");               
    }
    printf("\n");
    }
    
     
   getch();
   return 0;
}
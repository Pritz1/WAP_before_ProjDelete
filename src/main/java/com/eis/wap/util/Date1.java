package com.eis.wap.util;


public class Date1 {

	public static int getFinStrtYr(int strtMth,int logMth,int logYr){
		if(logMth < strtMth){ //login date=012017 -> (logmth)01 <= (endMth)0
			return (logYr-1); //2017
		}else{ //login date=042017 -> 04>03
			return logYr; //2016
		}
	}
	
	public static void main(String[] args) {
		String finStrtMth = "04";
		
		//String[] arrOfStr = finStrtMthYr .split("-"); 
		//int[] intRes1= {0,0};
		String logMth = "01";
		String logYr = "2019";
		//String[] arrOfStr1 = logYrMth.split("-"); 
		//int[] intRes2= {0,0};
        /*for (int i=0;i<2;i++)
        {
        	//intRes1[i]=Integer.parseInt(arrOfStr[i]);
        	intRes2[i]=Integer.parseInt(arrOfStr1[i]);
        }*/
        int endMth = Integer.parseInt(finStrtMth)-1; //endMth
        int strtYr = getFinStrtYr(Integer.parseInt(finStrtMth),Integer.parseInt(logMth),Integer.parseInt(logYr));
        int endYr=0;
        //int yr = strtYr+1;
        if(endMth==0)
        {
        	endMth=12;
        	endYr=strtYr;
        }else{
        	endYr=strtYr+1;
        }
        System.out.println("Financial Year is :"+finStrtMth+strtYr+" to "+endMth+""+endYr);
        /*if(intRes2[0]>=intRes1[0])
        {
        	 int mth1 = intRes1[0]-1;
             int yr1 = intRes2[1]+1;
             if(mth1==0)
             {
            	 mth1=12;
            	 yr1--;
             }
        	System.out.println("Financial Year is :"+intRes1[0]+"-"+intRes2[1]+" to "+mth1+"-"+yr1);
        }
        else
        {
        	int yr1 = intRes2[1]-1;
        	int mth1 = intRes1[0]-1;
        	if(mth1==13)
        	{
        		mth1=1;
        		yr1++;
        	}	
        	System.out.println("Financial Year is :"+intRes1[0]+"-"+yr1+" to "+mth1+"-"+intRes2[1]);
        }*/
	}

}

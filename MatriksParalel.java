/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matriks.paralel;

import java.util.Random;

/**
 *
 * @author nikobarani
 */
public class MatriksParalel {
    public static int akar;
    public static int[][] MatriksA;
    public static int[][] MatriksB;
    public static int[][] MatriksC;
    public static int x;
    
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
    //    System.out.println("Input jumlah baris dan kolom matriks : ");
      //  x = System.in.read();
        x = 5600;
        akar = (int) Math.sqrt(x);
        MatriksA = IsiMatriks(x);
        MatriksB = IsiMatriks(x);
        MatriksC = new int [x][x];
        long t1 = System.currentTimeMillis();
        iterasi1();       
        iterasi2();
        long t2 = System.currentTimeMillis();
       // printMatrix(MatriksC);
        //System.out.println("\nThe time is "+(t2-t1)+ " ms");
        System.out.println("\nThe time is "+(t2-t1)/1000 + "seconds");
        System.out.println("\nThe time is "+((t2-t1)/1000)/60 + "minutes");
    }
    
    public static void iterasi1() throws Exception{
        for(int i=1;i<akar;i++){
            for(int j=i*akar; j<(i+1)*akar; j++){
                geserKiri(MatriksA,j,i);
                geserAtas(MatriksB,j,i);
            }
        }
        multiplyMatrix();
        //nonParallelMultiply();
    }
    
    public static void iterasi2() throws Exception{
        for(int i=0;i<Math.sqrt(x)-1;i++){
            for(int j=0;j<Math.sqrt(x)*Math.sqrt(x);j++)
            {
                geserKiri(MatriksA,j,1);
                geserAtas(MatriksB,j,1);
            }
            multiplyMatrix();
            //nonParallelMultiply();
        }
    }  
    
    public static void geserKiri(int[][] matrix, int baris, int jumlahGeser){
        int temp;
        
        for(int i=0; i<akar*jumlahGeser; i++){
            for(int j=0; j<Math.pow(akar, 2)-1; j++){
                temp = matrix[baris][j];
                matrix[baris][j] = matrix[baris][j+1];
                matrix[baris][j+1] = temp;                
            }
        }
    }
    
    public static void geserAtas(int[][] matrix, int kolom, int jumlahGeser){
        int temp;
        
        for(int i=0; i<akar*jumlahGeser; i++){
            for(int j=0; j<Math.pow(akar, 2)-1; j++){
                temp = matrix[j][kolom];
                matrix[j][kolom] = matrix[j+1][kolom];
                matrix[j+1][kolom] = temp;                
            }            
        }
    }
    public static class thread extends Thread {
        
        int batasAtas;
        int batasBawah;
        int batasKiri;
        int batasKanan;
            
        public thread(int batasAtas, int batasBawah, int batasKiri, int batasKanan){
            this.batasAtas = batasAtas;
            this.batasBawah = batasBawah;
            this.batasKiri = batasKiri;
            this.batasKanan = batasKanan;
        }
        
        @Override
        public void run() {        

            for (int i = batasAtas; i < batasBawah; i++) {
                for (int j = batasKiri; j < batasKanan; j++) {
                    for (int k = 0; k < akar; k++) {
                        MatriksC[i][j] = MatriksC[i][j] + MatriksA[i][batasKiri+k] * MatriksB[batasAtas+k][j];
                    }
                }
            }
        }
    }
    
    public static void nonParallelMultiply()
     {
         int sum = 0;
            for ( int i = 0 ; i < x ; i++ ){
              for ( int j = 0 ; j < x ; j++ ){
                for ( int k = 0 ; k < x ; k++ ){
                  sum = sum + MatriksA[i][k]*MatriksB[k][j];
                }
                MatriksC[i][j] = sum;
                sum = 0;
              }
            }
     }
    
    public static void multiplyMatrix() throws Exception{
        thread m[] = new thread[(int)Math.pow(akar, 2)];
        
        for(int i=0; i<akar; i++){
            for(int j=0; j<akar; j++){
                m[i*akar+j] = new thread(i * akar,(i+1)*akar,j*akar,(j+1)*akar);
                try {
                    m[i*akar+j].start();                    
                } catch (Exception e) {
                    System.out.print("Error Thread");
                }
            }            
        }
        for(int i=0;i<Math.pow(akar, 2);i++){
            m[i].join();
        }
    }
    
    public static int[][] IsiMatriks(int x){

        Random rand = new Random();
        int matriks[][] = new int[x][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                matriks[i][j] = rand.nextInt(9);
            }
        }
        return matriks;
    }
   
    public static void printMatrix(int m[][]){       
        System.out.println("\nMatrix\n");
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.print("\n");
        }
    }
}

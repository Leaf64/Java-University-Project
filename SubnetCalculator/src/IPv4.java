/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import static printPackage.Printer.print;
import static printPackage.Printer.println;

public class IPv4 {

    private String ipString;
    private String maskString;


    private String ipBinaryString;
    private String maskBinaryString;

    private int[] ipOctets;
    private int[] maskOctets;


    private int[] netAddress;
    private String netClass;

    private boolean isPrivate;

    private String maskDecimal;
    private String binaryMask;

    private int[] broadcastOctets;
    private String broadcastBinary;

    public String getIpString() {
        return ipString;
    }

    public String getMaskString() {
        return maskString;
    }

    public String getIpBinaryString() {
        return ipBinaryString;
    }

    public String getMaskBinaryString() {
        return maskBinaryString;
    }

    public int[] getIpOctets() {
        return ipOctets;
    }

    public int[] getMaskOctets() {
        return maskOctets;
    }

    IPv4(String ip, String mask){

        String[] octetsString = ipString.split("\\.");

        ipOctets = octetsString_to_octetsInt(octetsString);
        //Now we have IP octets in INTEGER

        maskOctets = mask_in_CIDR_to_mask_in_octetsInt(mask);
        //Now we have mask octets in INTEGER


        netAddress = new int[4];
        for(int i=0; i<4;i++){
            netAddress[i] = ipOctets[i]&maskOctets[i];    //AND'ing ip with mask will give netAddress
        }


        ipBinaryString = octetsInt_to_binaryString(ipOctets); //first convert it to binary
        netClass = checkNetClass(ipBinaryString);


        isPrivate = IPisPrivate(ipOctets);


        //4================================ MASK IN DECIMAL AND BINARY ===============================
        maskDecimal  = "";
        for(int i=0; i<4; i++){
            maskDecimal  = maskDecimal + maskOctets[i];
            if(i<3) maskDecimal = maskDecimal + ".";
        }

        binaryMask = mask_in_CIDR_to_mask_in_binaryString_withDots(mask);


        //5 ============================= BROADCAST ADDRESS =========================================
        broadcastOctets = giveBroadcastAddress(netAddress, maskOctets);

        broadcastBinary = "";

        for(int i=0; i<4; i++){
            broadcastBinary = broadcastBinary +  broadcastOctets[i];
            if(i<3) broadcastBinary = broadcastBinary + ".";
        }
        println("");
        print("BINARY BRODCAST ADDRESS: ");
        //println();
        println(octetsInt_to_binaryString(broadcastAddress));
        println("");
        //5============================== BROADCAST ADDRESS =========================================


        //6============================== FIRST HOST ================================================
        println("6.");
        int [] octetsIntfirstHost = firstHost(netAddress);
        print("First host address = " );
        for(int i=0; i<4; i++) {
            print(octetsIntfirstHost[i]);
            if(i<3) print(".");
        }
        println("");
        println("Decimal: " + octetsInt_to_binaryString(octetsIntfirstHost));

        //6============================== FIRST HOST ================================================

        //7============================== LAST HOST =================================================
        println("7.");
        int [] octetsIntLastHost = lastHost(broadcastAddress);
        print("Last host address = " );
        for(int i=0; i<4; i++) {
            print(octetsIntLastHost[i]);
            if(i<3) print(".");
        }
        println("");
        println("Decimal: " + octetsInt_to_binaryString(octetsIntLastHost));
        //7============================== LAST HOST =================================================

        //8============================== MAX HOSTS NUMBER ===========================================
        println(Integer.parseInt(mask));
        println("Max hosts number: " + maxHostNumber(Integer.parseInt(mask)));
        //8============================== MAX HOSTS NUMBER ===========================================


    }


    public static String octetsInt_to_binaryString(int[] octetsInt){
        String binaryString ="";
        for(int i=0; i<4; i++){
            binaryString = binaryString+ String.format("%8s", Integer.toBinaryString(octetsInt[i])).replace(' ', '0');
            if(i<3) binaryString = binaryString + ".";
        }
        return binaryString;
    }




    public static void askUserForPing(String IPaddress) throws IOException {
        System.out.println("Do you want to ping? Y/N");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(answer.equals("Y") || answer.equals("y") || answer.equals("yes") || answer.equals("Yes") || answer.equals("YES"))
        {
            requestPing(IPaddress);
        } else
        {
            System.out.println("Ok, I will not ping");
        }

    }

    public static void requestPing(String IP) throws IOException {
        String s;
        String command = "ping " + IP;
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while((s = reader.readLine()) != null){
            //if(s.contains("Rep") || s.contains("Req")){
            println(s);

            // }
        }
    }


    public static int maxHostNumber(int CIDRmask){

        //Formula for it:
        // 2^(amount of bits in IP - mask in CIDR) -2
        //I do not want to implement Math and any other math library, just for fun
        int power = 32-CIDRmask;
        int result =2;
        for(int i=0; i<power-1;i++){
            result = result*2;
        }
        result = result - 2;
        return result;
    }

    public static int[] lastHost(int[] octetsIntBroadcast){
        //BROADCAS ADDRESS CANNOT HAVE 00000000 IN THE LAST OCTET SO:
        int[] octetsLastHost = octetsIntBroadcast;
        octetsLastHost[3]--;
        return  octetsLastHost;
    }


    public static int[] firstHost(int[] octetsIntNetAddress){
        //NET ADDRESS CANNOT HAVE 1 ON THE LAST BIT SO:
        int[] octetsFirstHost = octetsIntNetAddress;
        octetsFirstHost[3]++;
        return octetsFirstHost;
    }


    public static int not(int a)
    {
        return 255 -a;
    }


    public static int[] giveBroadcastAddress(int[]octetsIntNetAddress, int[]octetsIntMask) {
        int[] octetsIntBroadcast = new int[4];
        for(int i =0; i<4; i++) {
            octetsIntBroadcast[i] = octetsIntNetAddress[i] + not(octetsIntMask[i]);
        }
        return octetsIntBroadcast;
    }



    public static boolean IPisPrivate(int[] octetsInt){

        if(octetsInt[0] == 10){   //  range: 10.0.0.0 – 10.255.255.255
            return true;
        } else if(octetsInt[0] == 172 && octetsInt[1] >= 16 && octetsInt[1] <= 31){ // range:  172.16.0.0 – 172.31.255.255
            return true;
        } else if(octetsInt[0] == 192 && octetsInt[1] == 168){    // range:	172.16.0.0 – 172.31.255.255
            return true;
        }
        return false;
    }



    public static String checkNetClass(String binaryString) {
        if(binaryString.substring(0,1).equals("0")){
            return "A"; //if first bit is 0 then class is A
        } else if(binaryString.substring(0,2).equals("10")) {
            return "B"; //if first two bits are 10 then class is B
        } else if(binaryString.substring(0,3).equals("110")){
            return "C";  //if first 3 bits are 110 then class is C etc.
        } else if(binaryString.substring(0,4).equals("1110")) {
            return "D";  //etc
        } else if(binaryString.substring(0,4).equals("1111")){
            return "E";  //etc
        }
        return null;
    }




    public static int[] octetsString_to_octetsInt(String[] octetsString) {
        int[] octetsInt = new int[4];
        octetsInt[0] = Integer.parseInt(octetsString[0]);
        octetsInt[1] = Integer.parseInt(octetsString[1]);
        octetsInt[2] = Integer.parseInt(octetsString[2]);
        octetsInt[3] = Integer.parseInt(octetsString[3]);
        return octetsInt;
    }

    public static String octetsInt_to_decimalString(int[] octetsInt) {
        return octetsInt[0] + "." + octetsInt[1] + "." + octetsInt[2] + "." + octetsInt[3];
    }

    public static int[] mask_in_CIDR_to_mask_in_octetsInt(String CIDR)
    {
        String binaryString = mask_in_CIDR_to_mask_in_binaryString(CIDR);
        //We have binary, now cut in to the 4 binaryString formats
        String[] octetsString = new String[4];
        octetsString[0] = binaryString.substring(0, 8);
        octetsString[1] = binaryString.substring(8, 16);
        octetsString[2] = binaryString.substring(16, 24);
        octetsString[3] = binaryString.substring(24, 32);
        //Now convert 4 binaryString to octetsInt
        int[] octetsInt = new int[4];
        octetsInt[0] = Integer.parseInt(octetsString[0],2);
        octetsInt[1] = Integer.parseInt(octetsString[1],2);
        octetsInt[2] = Integer.parseInt(octetsString[2],2);
        octetsInt[3] = Integer.parseInt(octetsString[3],2);
        return octetsInt;
    }

    public static String mask_in_CIDR_to_mask_in_binaryString(String CIDR)
    {
        int CIDR_int = Integer.parseInt(CIDR);
        String binaryString ="";
        int i=0;
        for(  ; i<CIDR_int; i++) {  //this will fulfill String with 1's because CIDR notation show amount on 1's in binary notation
            binaryString = binaryString + "1";

        }
        int zerosAmount = 32  - i;  //this will create amount of zeros because the rest of 32-lenght binary will be zeros
        for(int j = 0; j<zerosAmount;j++) {
            binaryString = binaryString + "0";
        }
        return binaryString;
    }

    public static String mask_in_CIDR_to_mask_in_binaryString_withDots(String CIDR)
    {
        int CIDR_int = Integer.parseInt(CIDR);
        String binaryString ="";
        int i=0;
        int dots =0;
        for(  ; i<CIDR_int; i++) {  //this will fulfill String with 1's because CIDR notation show amount on 1's in binary notation
            binaryString = binaryString + "1";
            dots++;
            if(dots==8 || dots==16 || dots == 24) binaryString = binaryString+".";
        }
        int zerosAmount = 32  - i;  //this will create amount of zeros because the rest of 32-lenght binary will be zeros
        for(int j = 0; j<zerosAmount;j++) {
            binaryString = binaryString + "0";
            dots++;
            if(dots==8 || dots==16 || dots == 24) binaryString = binaryString+".";
        }
        return binaryString;
    }



}
*/
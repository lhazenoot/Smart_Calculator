package practices;

public class FindRightPattern {
    public static void main(String[] args) {
        String beast = "beast";
        String best = "best";
        String east = "east";

        boolean check1 = beast.matches("bea?s?t?");
        boolean check2 = best.matches("bea?s?t?");
        boolean check3 = east.matches("bea?s?t?");
        System.out.println(check1);
        System.out.println(check2);
        System.out.println(check3);
    }
}

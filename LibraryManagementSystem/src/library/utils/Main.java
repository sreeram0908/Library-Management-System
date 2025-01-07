@FunctionalInterface
interface Calculator {
    void operator(int a, int b);
}

sealed class Human permits girl, boy {
    String name;

    Human(String name) {
        this.name = name;
    }
}

final class boy extends Human {
    String gen;

    boy(String name, String gen) {
        super(name);
        this.gen = gen;
    }

    void disn() {
        System.out.println(name + " " + gen);
    }
}

final class girl extends Human {
    String gen;

    girl(String name, String gen) {
        super(name);
        this.gen = gen;
    }

    void disn() {
        System.out.println(name + " " + gen);
    }
}

public class Main {
    Calculator sum = (a, b) -> System.out.println(a + b);

    static void display(Human ob) {
        switch (ob) {
            case boy s && s.gen.equals("male") -> s.disn();
                case girl g -> g.disn();
                default -> System.out.println("try a");
        }
    }

    public static void main(String[] args) {
        Main ob1 = new Main();
        ob1.sum.operator(10, 50);

        boy b1 = new boy("Sree", "male");
        girl g1 = new girl("Priya", "female");
        display(b1);
        display(g1);
    }
}

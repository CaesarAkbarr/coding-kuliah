class karakterGame {

    String nama;
    int hp, attackPoint;

    void infoKarakter() {

        System.out.println("Nama hero: " + nama);
        System.out.println("HP: " + hp);
        System.out.println("Attack Point: " + attackPoint);

    }
}

class warrior extends karakterGame {

    int defense;

    void statusWarrior() {

        int totalHp = hp + defense;
        System.out.println("Defense: " + defense);
        System.out.println("Total HP: " + totalHp);

    }

}

class mage extends karakterGame {

    int mana;

    void statusMage() {

        int totalAttack = attackPoint + mana;
        System.out.println("Mana: " + mana);
        System.out.println("Total Attack: " + totalAttack);

    }

}

public class latihanGame {

    public static void main(String[] args) {

        

    }

}

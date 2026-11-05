class karakterGame {

    String nama;
    int hp, attackPoint;

    void infoKarakter() {

        system.out.println("Nama hero: " + nama);
        system.out.println("HP: " + hp);
        system.out.println("Attack Point: " + attackPoint);

    }
}

class warrior extends karakterGame {

    int defense;

    void statusWarrior() {

        int totalHp = hp + defense;
        system.out.println("Defense: " + defense);
        system.out.println("Total HP: " + totalHp);

    }

}

class mage extends karakterGame {

    int mana;

    void statusMage() {

        int totalAttack = attackPoint + mana;
        system.out.println("Mana: " + mana);
        system.out.println("Total Attack: " + totalAttack);

    }

}

public class latihanGame {

    public static void main(String[] args) {



    }

}

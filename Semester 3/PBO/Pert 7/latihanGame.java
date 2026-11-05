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

        warrior hero1 = new warrior();

        hero1.nama = "Hylos";
        hero1.attackPoint = 5;
        hero1.hp = 200;

        hero1.infoKarakter();
        hero1.statusWarrior();


    }

}

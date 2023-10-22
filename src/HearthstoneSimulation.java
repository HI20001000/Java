import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Role {
    private int health;
    private int attack;

    public Role(int health, int attack) {
        this.health = health;
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }
}

class Player {
    private int heroHealth;
    private List<Role> battlefield;

    public Player() {
        heroHealth = 30;
        battlefield = new ArrayList<>();
    }

    public int getHeroHealth() {
        return heroHealth;
    }

    public List<Role> getBattlefield() {
        return battlefield;
    }

    public void summonMinion(int position, int attack, int health) {
        Role minion = new Role(health, attack);
        battlefield.add(position - 1, minion);
    }

    public void attack(int attackerIndex, int defenderIndex, Player opponent) {
        Role attacker = battlefield.get(attackerIndex - 1);
        Role defender;

        if (defenderIndex == 0) {
            defender = new Role(opponent.getHeroHealth(), 0);
        } else {
            defender = opponent.getBattlefield().get(defenderIndex - 1);
        }

        attacker.takeDamage(defender.getAttack());
        defender.takeDamage(attacker.getAttack());

        if (attacker.isDead()) {
            battlefield.remove(attackerIndex - 1);
        }
        if (defender.isDead() && defenderIndex != 0) {
            opponent.getBattlefield().remove(defenderIndex - 1);
        }
    }

    public boolean isDead() {
        return heroHealth <= 0;
    }
}

public class HearthstoneSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Player firstPlayer = new Player();
        Player secondPlayer = new Player();
        boolean isFirstPlayerTurn = true;

        for (int i = 0; i < n; i++) {
            String action = scanner.next();

            if (action.equals("summon")) {
                int position = scanner.nextInt();
                int attack = scanner.nextInt();
                int health = scanner.nextInt();
                if (isFirstPlayerTurn) {
                    firstPlayer.summonMinion(position, attack, health);
                } else {
                    secondPlayer.summonMinion(position, attack, health);
                }
            } else if (action.equals("attack")) {
                int attacker = scanner.nextInt();
                int defender = scanner.nextInt();
                if (isFirstPlayerTurn) {
                    firstPlayer.attack(attacker, defender, secondPlayer);
                } else {
                    secondPlayer.attack(attacker, defender, firstPlayer);
                }
            } else if (action.equals("end")) {
                isFirstPlayerTurn = !isFirstPlayerTurn;
            }
        }

        int result = 0;
        if (firstPlayer.isDead()) {
            result = -1;
        } else if (secondPlayer.isDead()) {
            result = 1;
        }

        System.out.println(result);
        System.out.println(firstPlayer.getHeroHealth());
        System.out.print(firstPlayer.getBattlefield().size() + " ");
        for (Role minion : firstPlayer.getBattlefield()) {
            System.out.print(minion.getHealth() + " ");
        }
        System.out.println();
        System.out.println(secondPlayer.getHeroHealth());
        System.out.print(secondPlayer.getBattlefield().size() + " ");
        for (Role minion : secondPlayer.getBattlefield()) {
            System.out.print(minion.getHealth() + " ");
        }
    }
}

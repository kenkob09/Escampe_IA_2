package modeles;

/**
 * L'interface d'un état est succinte, tout ce dont on a
 * besoin pour un état est :
 * 	- qu'il s'affiche
 * 	- et que la fonction equals soit redéfini de manière adéquate.
 */
public interface Etat {

    @Override
    boolean equals(Object obj);

    @Override
    String toString();
}

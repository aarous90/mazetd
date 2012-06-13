/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * MazeTD Project (c) 2012 by Hady Khalifa, Ahmed Arous and Hans Ferchland
 * 
 * MazeTD rights are by its owners/creators.
 * The project was created for educational purposes and may be used under 
 * the GNU Public license only.
 * 
 * If you modify it please let other people have part of it!
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * GNU Public License
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 * Email us: 
 * hans[dot]ferchland[at]gmx[dot]de
 * 
 * 
 * Project: MazeTD Project
 * File: Level.java
 * Type: logic.Level
 * 
 * Documentation created: 22.05.2012 - 20:38:11 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.scene.Node;
import entities.Map;
import entities.Map.MapSquare;
import entities.Orb;
import entities.Stone;
import entities.Tower;
import entities.base.EntityManager;
import eventsystem.EventManager;
import eventsystem.port.Collider3D;
import eventsystem.port.ScreenRayCast3D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import logic.pathfinding.CreepAI;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 * The class Level is the main context of the game. It manages all 
 * enitiy creation and destruction.
 * @author Hans Ferchland
 */
public class Level {

    //==========================================================================
    //===   Singleton
    //==========================================================================
    /**
     * The hidden constructor of Level.
     */
    private Level() {
    }

    /**
     * The static method to retrive the one and only instance of Level.
     */
    public static Level getInstance() {
        return LevelHolder.INSTANCE;
    }

    /**
     * The holder-class LevelHolder for the Level.
     */
    private static class LevelHolder {

        private static final Level INSTANCE = new Level();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private MazeTDGame game = MazeTDGame.getInstance();
    private Player player = Player.getInstance();
    private boolean initialized = false;
    private EntityManager entityManager = EntityManager.getInstance();
    private EventManager eventManager = EventManager.getInstance();
    private CreepAI creepAI = CreepAI.getInstance();
    private ScreenRayCast3D rayCast3D = ScreenRayCast3D.getInstance();
    private Collider3D collider3D = Collider3D.getInstance();
    private WaveManager waveManager = WaveManager.getInstance();
    private Pathfinder pathfinder = Pathfinder.getInstance();
    private Node mainLevelNode;
    private Node staticLevelElements;
    private Node dynamicLevelElements;
    private Map map = Map.getInstance();
    private Grid grid = Grid.getInstance();
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the level for the first time or after destroyed.
     */
    public void initialize() {
        if (initialized) {
            return;
        }

        rayCast3D.initialize();
        collider3D.initialize();

        mainLevelNode = new Node("MainLevelNode");
        staticLevelElements = new Node("StaticLevelElements");
        dynamicLevelElements = new Node("DynamicLevelElements");
        mainLevelNode.attachChild(staticLevelElements);
        mainLevelNode.attachChild(dynamicLevelElements);

        map.initialize();
        pathfinder.initialize();
        createStones(map.getMapSquares());
        setupLevelContent();

        TowerListener t = new TowerListener();

        game.getIsoCameraControl().lookAtMapSquare(
                pathfinder.getStartField().getSquare());
        game.getRootNode().attachChild(mainLevelNode);
        initialized = true;
    }

    /**
     * Setup the level for a new game.
     */
    private void setupLevelContent() {

        // Setup Grid and Map

        staticLevelElements.attachChild(map);

        waveManager.loadWaves(testWaves());
//        waveManager.setStartWave(6);
        waveManager.initialize();

//        Orb o = entityManager.createOrb(
//                "FirstOrb", new Vector3f(2, 0, 1), Orb.ElementType.GREEN);

//        Tower t = buildTower(Pathfinder.getInstance().getStartField().getSquare());
//        t.placeOrb(Orb.ElementType.RED);
//        t.placeOrb(Orb.ElementType.GREEN);
//        t.placeOrb(Orb.ElementType.YELLOW);

//        Creep c = entityManager.createCreep("FirstCreep",
//                Pathfinder.getInstance().getStartField().getSquare().getLocalTranslation(),
//                100, 100);


    }

    /**
     * Init waves for simple waves. 
     * Maybe import from XML or via (random) seed.
     * @return the waves to use for the game
     */
    private Queue<WaveManager.WaveDescription> testWaves() {
        LinkedList<WaveManager.WaveDescription> waveDescriptions =
                new LinkedList<WaveManager.WaveDescription>();

        WaveManager.WaveDescription description;

        for (int i = 0; i < 30; i++) {
            description = waveManager.new WaveDescription();
            description.creepCount = 5 + (i * 5);
            description.creepDamage = 80 + 15 * i;
            description.creepGoldDrop = 5 + 2 * i;
            description.creepOrbDropRate = 0.03f + 0.01f * i;
            description.creepSpeed = 0.8f + 0.05f * i;
            description.maxCreepHealthPoints = 20 + 12.5f * i;

            waveDescriptions.add(description);
        }
        return waveDescriptions;
    }

    /**
     * Updates the level components.
     * @param tpf 
     */
    public void update(float tpf) {
        player.update(tpf);
        pathfinder.update(tpf);
        creepAI.update(tpf);
        entityManager.update(tpf);
    }

    /**
     * Destroys all level attributes so it will have to be initialized again.
     */
    public void destroy() {

        collider3D.destroy();
        rayCast3D.destroy();

        staticLevelElements.detachAllChildren();
        dynamicLevelElements.detachAllChildren();
        mainLevelNode.detachAllChildren();

        game.getRootNode().detachChild(mainLevelNode);
        initialized = false;
    }

    /**
     * Checks if the level is initiliazed already.
     * @return true if initialize() was called, false if destroy() was called
     * or initialize() was not called.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Erzeugt ein grafisches Tower-Objekt an der Position des übergebenen Squares
     * - Liegt der erzeugte Tower auf dem Path wird ein neuer Path generiert 
     *   und allen Creeps übergeben
     * 
     * @param square 
     */
    public Tower buildTower(MapSquare square) {
        // add tower
        Tower t = entityManager.createTower(
                "FirstTower", square);
//       t.placeOrb(Orb.ElementType.RED, 0);
//        t.placeOrb(Orb.ElementType.YELLOW, 1);
//        t.placeOrb(Orb.ElementType.GREEN,2);

        creepAI.setChangeMapSquare(square, Pathfinder.TOWER_WEIGHT);
        return t;
    }

    private void createStones(HashSet<MapSquare> mapSquares) {
        for (MapSquare s : mapSquares) {
            if (Math.random() < 0.07) {
                s.buildStoneOnField();
            }

        }
    }

    /**
     * Erzeugt ein grafisches Stone-Objekt an der Position des übergebenen Squares
     * - Stone-Objekte sind unzerstoerbar und durchquerbar
     * 
     * @param square 
     */
    public Stone buildStone(MapSquare square) {
        // add tower
        Stone s = entityManager.createStone(
                "Stone", square);

        getStaticLevelElements().attachChild(s.getGeometryNode());
        square.getFieldInfo().incrementWeight(pathfinder.STONE_WEIGHT);
        return s;
    }

    /**
     * Removes a tower from a given MapSquare and recalculates the creep-pathes.
     * 
     * @param square the square containing the tower
     */
    public void removeTower(MapSquare square) {
        if (square != null && square.getTower() != null) {
            creepAI.setChangeMapSquare(square, -Pathfinder.TOWER_WEIGHT);
        }
    }

    /**
     * Retrieves the node where all dynamic elements are stored.
     * @return the jme3 node
     */
    public Node getDynamicLevelElements() {
        return dynamicLevelElements;
    }

    /**
     * Retrieves the node where all static elements are stored.
     * @return the jme3 node
     */
    public Node getStaticLevelElements() {
        return staticLevelElements;
    }
}

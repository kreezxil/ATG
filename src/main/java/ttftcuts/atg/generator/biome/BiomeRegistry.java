package ttftcuts.atg.generator.biome;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import ttftcuts.atg.ATG;
import ttftcuts.atg.ATGBiomes;
import ttftcuts.atg.generator.CoreNoise;
import ttftcuts.atg.util.MathUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class BiomeRegistry {

    public Map<EnumBiomeCategory, Map<String, BiomeGroup>> biomeGroups;
    public Map<Biome, Map<Biome, Double>> subBiomes;
    public Map<Biome, Double> subWeightTotals;
    public Map<Biome, Map<Biome, Double>> hillBiomes; // sub lists are assumed to be ordered lowest to highest!
    public Map<Biome, HeightModEntry> heightMods;

    public BiomeRegistry() {
        this.biomeGroups = new HashMap<EnumBiomeCategory, Map<String, BiomeGroup>>();
        this.subBiomes = new HashMap<Biome, Map<Biome, Double>>();
        this.subWeightTotals = new HashMap<Biome, Double>();
        this.hillBiomes = new HashMap<Biome, Map<Biome, Double>>();
        this.heightMods = new HashMap<Biome, HeightModEntry>();

        for (EnumBiomeCategory category : EnumBiomeCategory.values()) {
            this.biomeGroups.put(category, new HashMap<String, BiomeGroup>());
        }

        this.populate();
    }

    public void populate() {
        //TODO: Here's where the settings would apply packs of changes or whatever on top of the default, but for now it's all pre-set

        Random rand = new Random();

        //------ Land -----------------------

        // Plains
        addGroup(EnumBiomeCategory.LAND, "Plains", 0.8, 0.4, 0.35)
                .addBiome(Biomes.PLAINS);

        // Desert
        addGroup(EnumBiomeCategory.LAND, "Desert", 1.8, 0.2, 0.275)
                .setBlobSizeModifier(1) // larger blobs, one power of two greater
                .addBiome(Biomes.DESERT)
                .addBiome(Biomes.MESA, 0.3);

        // Forest
        addGroup(EnumBiomeCategory.LAND, "Forest", 0.7, 0.8, 0.35)
                .addBiome(Biomes.FOREST)
                .addBiome(Biomes.BIRCH_FOREST, 0.3)
                .addBiome(Biomes.ROOFED_FOREST, 0.2);

        // Taiga
        addGroup(EnumBiomeCategory.LAND, "Taiga", 0.05, 0.7, 0.4) // height 0.5
                .addBiome(Biomes.COLD_TAIGA);

        // Ice Plains
        addGroup(EnumBiomeCategory.LAND, "Ice Plains", 0.0, 0.45, 0.3)
                .addBiome(Biomes.ICE_PLAINS);

        // Jungle
        addGroup(EnumBiomeCategory.LAND, "Jungle", 1.75, 0.75, 0.325)
                .addBiome(Biomes.JUNGLE);

        // Shrubland
        addGroup(EnumBiomeCategory.LAND, "Shrubland", 0.77, 0.53, 0.35)
                .addBiome(ATGBiomes.SHRUBLAND);

        // Boreal Forest
        addGroup(EnumBiomeCategory.LAND, "Boreal Forest", 0.4, 0.8, 0.4) // temp 0.25, height 0.35
                .addBiome(Biomes.TAIGA)
                .addBiome(Biomes.REDWOOD_TAIGA, 0.4);

        // Tundra
        addGroup(EnumBiomeCategory.LAND, "Tundra", 0.25, 0.45, 0.325)
                .addBiome(ATGBiomes.TUNDRA);

        // Savanna
        addGroup(EnumBiomeCategory.LAND, "Savanna", 1.7, 0.55, 0.275)
                .setSubBlobSizeModifier(1)
                .addBiome(Biomes.SAVANNA);

        // Tropical Shrubland
        addGroup(EnumBiomeCategory.LAND, "Tropical Shrubland", 1.75, 0.65, 0.35)
                .addBiome(ATGBiomes.TROPICAL_SHRUBLAND);

        // Woodland
        addGroup(EnumBiomeCategory.LAND, "Woodland", 0.7, 0.67, 0.3)
                .addBiome(ATGBiomes.WOODLAND);

        // Dry Scrubland
        addGroup(EnumBiomeCategory.LAND, "Dry Scrubland", 1.8, 0.35, 0.325)
                .addBiome(ATGBiomes.SCRUBLAND);

        //------ Beach -----------------------

        // Beach
        addGroup(EnumBiomeCategory.BEACH, "Beach", 0.6, 0.4, 0.25) // 0.8, 0.4, 0.25
                .addBiome(Biomes.BEACH);

        // Stone Beach
        addGroup(EnumBiomeCategory.BEACH, "Cold Beach", 0.34, 0.5, 0.25) // 0.25, 0.4, 0.25
                .addBiome(ATGBiomes.GRAVEL_BEACH);

        // Cold Beach
        addGroup(EnumBiomeCategory.BEACH, "Snowy Beach", 0.0, 0.5, 0.26) // 0.0, 0.4, 0.25
                .addBiome(ATGBiomes.GRAVEL_BEACH_SNOWY);



        //------ Swamplands -----------------------

        // Swampland
        addGroup(EnumBiomeCategory.SWAMP, "Swampland", 0.8, 0.9, 0.25)
                .addBiome(Biomes.SWAMPLAND);



        //------ Ocean -----------------------

        Double deep = 28.0 / 255.0;

        // Ocean
        addGroup(EnumBiomeCategory.OCEAN, "Ocean", 0.5, 0.5, 0.25, deep, 1.0)
                .addBiome(Biomes.OCEAN);

        // Deep Ocean
        addGroup(EnumBiomeCategory.OCEAN, "Deep Ocean", 0.5, 0.5, 0.25, 0.0, deep)
                .addBiome(Biomes.DEEP_OCEAN)
                .addBiome(Biomes.MUSHROOM_ISLAND, 0.002);


        //------ SUB-BIOMES -----------------------

        // mutations
        double mutation = 1.0/15.0;

        addSubBiome(Biomes.PLAINS, Biomes.MUTATED_PLAINS, mutation);
        addSubBiome(Biomes.DESERT, Biomes.MUTATED_DESERT, mutation);
        addSubBiome(Biomes.EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS, mutation);
        addSubBiome(Biomes.EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, mutation);
        addSubBiome(Biomes.FOREST, Biomes.MUTATED_FOREST, mutation);
        addSubBiome(Biomes.FOREST, Biomes.ROOFED_FOREST, mutation);
        addSubBiome(Biomes.FOREST_HILLS, Biomes.MUTATED_FOREST, mutation);
        addSubBiome(Biomes.ROOFED_FOREST, Biomes.MUTATED_ROOFED_FOREST, mutation);
        addSubBiome(Biomes.TAIGA, Biomes.MUTATED_TAIGA, mutation);
        addSubBiome(Biomes.TAIGA, Biomes.FOREST, mutation);
        addSubBiome(Biomes.ICE_PLAINS, Biomes.MUTATED_ICE_FLATS, mutation);
        addSubBiome(Biomes.BIRCH_FOREST, Biomes.MUTATED_BIRCH_FOREST, mutation);
        addSubBiome(Biomes.BIRCH_FOREST_HILLS, Biomes.MUTATED_BIRCH_FOREST_HILLS, mutation);
        addSubBiome(Biomes.COLD_TAIGA, Biomes.MUTATED_TAIGA_COLD, mutation);
        addSubBiome(Biomes.REDWOOD_TAIGA, Biomes.MUTATED_REDWOOD_TAIGA, mutation);
        addSubBiome(Biomes.REDWOOD_TAIGA_HILLS, Biomes.MUTATED_REDWOOD_TAIGA_HILLS, mutation);
        addSubBiome(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, mutation * 0.5);
        addSubBiome(Biomes.SAVANNA, Biomes.MUTATED_SAVANNA, mutation * 0.15);
        addSubBiome(Biomes.SAVANNA, Biomes.MUTATED_SAVANNA_ROCK, mutation * 0.15);
        addSubBiome(Biomes.MESA, Biomes.MUTATED_MESA_ROCK, mutation);
        addSubBiome(Biomes.MESA, Biomes.MUTATED_MESA_CLEAR_ROCK, mutation);

        // mesa plateaus
        double mesa_plateaus = 0.25;
        addSubBiome(Biomes.MESA, Biomes.MESA_ROCK, mesa_plateaus); // plateau F
        addSubBiome(Biomes.MESA, Biomes.MESA_CLEAR_ROCK, mesa_plateaus); // plateau
        addSubBiome(Biomes.MESA, Biomes.MUTATED_MESA, mesa_plateaus); // bryce

        // copses and clearings
        double clearing = 0.10;
        addSubBiome(Biomes.PLAINS, ATGBiomes.WOODLAND, clearing);
        addSubBiome(Biomes.PLAINS, ATGBiomes.SHRUBLAND, clearing);
        addSubBiome(ATGBiomes.SHRUBLAND, ATGBiomes.WOODLAND, clearing);
        addSubBiome(ATGBiomes.SHRUBLAND, Biomes.FOREST, clearing);
        addSubBiome(ATGBiomes.TUNDRA, Biomes.TAIGA, clearing);
        addSubBiome(Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_WITH_TREES, clearing);

        addSubBiome(Biomes.FOREST, Biomes.PLAINS, clearing);
        addSubBiome(Biomes.FOREST, ATGBiomes.WOODLAND, clearing);
        addSubBiome(Biomes.FOREST, ATGBiomes.SHRUBLAND, clearing);
        addSubBiome(Biomes.FOREST_HILLS, Biomes.PLAINS, clearing);
        addSubBiome(Biomes.FOREST_HILLS, ATGBiomes.WOODLAND, clearing);
        addSubBiome(Biomes.FOREST_HILLS, ATGBiomes.SHRUBLAND, clearing);
        addSubBiome(Biomes.BIRCH_FOREST, Biomes.PLAINS, clearing);
        addSubBiome(Biomes.BIRCH_FOREST, ATGBiomes.SHRUBLAND, clearing);
        addSubBiome(Biomes.BIRCH_FOREST_HILLS, Biomes.PLAINS, clearing);
        addSubBiome(Biomes.BIRCH_FOREST_HILLS, ATGBiomes.SHRUBLAND, clearing);
        addSubBiome(Biomes.ROOFED_FOREST, ATGBiomes.WOODLAND, clearing);
        addSubBiome(Biomes.ROOFED_FOREST, ATGBiomes.SHRUBLAND, clearing);
        addSubBiome(Biomes.TAIGA, Biomes.PLAINS, clearing*2);
        addSubBiome(Biomes.TAIGA_HILLS, Biomes.PLAINS, clearing*2);
        addSubBiome(Biomes.COLD_TAIGA, Biomes.ICE_PLAINS, clearing*2);
        addSubBiome(Biomes.COLD_TAIGA_HILLS, Biomes.ICE_MOUNTAINS, clearing*2);
        addSubBiome(Biomes.ICE_MOUNTAINS, Biomes.COLD_TAIGA_HILLS, clearing);

        //------ HILL BIOMES -----------------------

        double hills = 128/255.0;
        double upperhills = 170/255.0;
        double mountain = 192/255.0;

        addHillBiome(Biomes.PLAINS, Biomes.EXTREME_HILLS, upperhills);
        addHillBiome(Biomes.FOREST, Biomes.FOREST_HILLS, hills);
        addHillBiome(Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, hills);
        addHillBiome(Biomes.TAIGA, Biomes.TAIGA_HILLS, hills);
        addHillBiome(Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, hills);
        addHillBiome(Biomes.COLD_TAIGA, Biomes.ICE_MOUNTAINS, mountain);
        addHillBiome(Biomes.JUNGLE, Biomes.JUNGLE_HILLS, hills);
        addHillBiome(Biomes.ICE_PLAINS, Biomes.ICE_MOUNTAINS, mountain);
        addHillBiome(ATGBiomes.TUNDRA, Biomes.EXTREME_HILLS, upperhills);
        addHillBiome(ATGBiomes.TUNDRA, Biomes.ICE_MOUNTAINS, mountain);
        addHillBiome(ATGBiomes.SHRUBLAND, Biomes.EXTREME_HILLS, mountain);
        addHillBiome(Biomes.DESERT, Biomes.DESERT_HILLS, hills);


        //------ HEIGHT MODIFIERS -----------------------

        addHeightModifier(Biomes.DESERT, ATGBiomes.HeightModifiers.DUNES);
        addHeightModifier(Biomes.MUSHROOM_ISLAND, ATGBiomes.HeightModifiers.ISLAND);
        addHeightModifier(Biomes.SAVANNA_PLATEAU, ATGBiomes.HeightModifiers.PLATEAU);
        addHeightModifier(Biomes.MUTATED_SAVANNA, ATGBiomes.HeightModifiers.PLATEAU, 1);
        addHeightModifier(Biomes.MUTATED_SAVANNA_ROCK, ATGBiomes.HeightModifiers.PLATEAU, 1);
        addHeightModifier(Biomes.MUTATED_ROOFED_FOREST, ATGBiomes.HeightModifiers.PLATEAU);

        addHeightModifier(Biomes.MESA, ATGBiomes.HeightModifiers.MESA);
        addHeightModifier(Biomes.MESA_ROCK, ATGBiomes.HeightModifiers.MESA, 1); // plateau F
        addHeightModifier(Biomes.MESA_CLEAR_ROCK, ATGBiomes.HeightModifiers.MESA, 1); // plateau
        addHeightModifier(Biomes.MUTATED_MESA, ATGBiomes.HeightModifiers.MESA, 2); // bryce
        addHeightModifier(Biomes.MUTATED_MESA_ROCK, ATGBiomes.HeightModifiers.MESA, 1); // plateau F M
        addHeightModifier(Biomes.MUTATED_MESA_CLEAR_ROCK, ATGBiomes.HeightModifiers.MESA, 1); // plateau M
    }

    public BiomeGroup addGroup(EnumBiomeCategory category, String name, double temperature, double moisture, double height, double minHeight, double maxHeight) {
        BiomeGroup biomeGroup = new BiomeGroup(name, temperature, moisture, height, minHeight, maxHeight);

        this.biomeGroups.get(category).put(name, biomeGroup);

        return biomeGroup;
    }

    public BiomeGroup addGroup(EnumBiomeCategory category, String name, double temperature, double moisture, double height) {
        return this.addGroup(category, name, temperature, moisture, height, 0.0, 1.0);
    }

    public void addSubBiome(Biome parent, Biome subBiome, double weight) {
        if (!this.subBiomes.containsKey(parent)) {
            this.subBiomes.put(parent, new LinkedHashMap<Biome, Double>());
            this.subWeightTotals.put(parent, 0.0);
        }

        Map<Biome,Double> subs = this.subBiomes.get(parent);
        this.subWeightTotals.put(parent, this.subWeightTotals.get(parent) + weight);

        if (!subs.containsKey(subBiome)) {
            subs.put(subBiome, weight);
        } else {
            subs.put(subBiome, subs.get(subBiome) + weight);
        }

        //ATG.logger.info("Sub biomes for "+parent.getBiomeName()+": (total weight: "+this.subWeightTotals.get(parent)+") "+subs);
    }

    public Biome getSubBiome(Biome parent, double value) {
        if (!this.subBiomes.containsKey(parent)) {
            return parent;
        }

        double weight = MathUtil.clamp(value, 0.0, 1.0) * (this.subWeightTotals.get(parent) + 1.0);

        if (weight <= 1.0) {
            return parent;
        }

        weight -= 1.0;

        Map<Biome, Double> weights = this.subBiomes.get(parent);

        double total = 0.0;
        for (Map.Entry<Biome, Double> entry : weights.entrySet()) {
            total += entry.getValue();
            if (total >= weight) {
                return entry.getKey();
            }
        }

        return null; // shouldn't happen!
    }

    public void addHillBiome(Biome parent, Biome hillBiome, double height) {
        if (!this.hillBiomes.containsKey(parent)) {
            this.hillBiomes.put(parent, new LinkedHashMap<Biome, Double>());
        }

        Map<Biome, Double> hills = this.hillBiomes.get(parent);
        hills.put(hillBiome, height);
    }

    public Biome getHillBiome(Biome parent, CoreNoise noise, int x, int z) {
        if (!this.hillBiomes.containsKey(parent)) {
            return parent;
        }

        Map<Biome, Double> hills = this.hillBiomes.get(parent);

        double height = noise.getHeight(x,z) + noise.getRoughness(x,z) * 0.1;

        Biome biome = parent;

        for (Map.Entry<Biome,Double> e : hills.entrySet()) {
            if (height > e.getValue()) {
                biome = e.getKey();
            } else {
                return biome;
            }
        }

        return biome;
    }

    public void addHeightModifier(Biome biome, IBiomeHeightModifier mod, Map<String,Object> args) {
        this.heightMods.put(biome, new HeightModEntry(mod, args));
    }

    public void addHeightModifier(Biome biome, IBiomeHeightModifier mod) {
        this.addHeightModifier(biome, mod, null);
    }

    public void addHeightModifier(Biome biome, IBiomeHeightModifier mod, int variant) {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("variant", variant);
        this.addHeightModifier(biome, mod, args);
    }

    public HeightModEntry getHeightModifier(Biome biome) {
        return this.heightMods.get(biome);
    }

    //------ BiomeGroup type enum ---------------------------------------------------------

    public enum EnumBiomeCategory {
        LAND(Biomes.PLAINS),
        OCEAN(Biomes.OCEAN),
        BEACH(Biomes.BEACH),
        SWAMP(Biomes.SWAMPLAND),
        ;

        public final BiomeGroup fallback;

        EnumBiomeCategory(Biome fallback) {
            this.fallback = new BiomeGroup(this.name()+"_fallback", 0.5,0.5,0.25);
            this.fallback.addBiome(fallback);
        }
    }

    //------ BiomeGroup Class ---------------------------------------------------------

    public static class BiomeGroup {
        public String name;
        public double temperature;
        public double moisture;
        public double height;
        public double minHeight;
        public double maxHeight;
        public int blobSizeModifier = 0;
        public int subBlobSizeModfier = 0;

        public long salt;
        public int offsetx;
        public int offsetz;

        public Map<Biome, Double> biomes;
        public double totalweight = 0.0;

        public BiomeGroup(String name, double temperature, double moisture, double height, double minHeight, double maxHeight) {
            this.name = name;
            this.temperature = temperature;
            this.moisture = moisture;
            this.height = height;
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.salt = name.hashCode();

            Random rand = new Random(this.salt);

            this.offsetx = rand.nextInt();
            this.offsetz = rand.nextInt();

            //this.offsetx = (int)( ( MathUtil.xorShift64( 2846 * MathUtil.xorShift64(salt + 7391834) - salt ) ) % Integer.MAX_VALUE);
            //this.offsetz = (int)( ( MathUtil.xorShift64( 9672 * MathUtil.xorShift64(salt + 4517384) - salt ) ) % Integer.MAX_VALUE);

            this.biomes = new LinkedHashMap<Biome, Double>();
        }

        public BiomeGroup(String name, double temperature, double moisture, double height) {
            this(name, temperature, moisture, height, 0.0, 1.0);
        }

        public BiomeGroup(String name, double temperature, double moisture) {
            this(name, temperature, moisture, 0.5);
        }

        public BiomeGroup addBiome(Biome biome, double weight) {
            if (biome != null) {
                if (!this.biomes.containsKey(biome)) {
                    this.biomes.put(biome, weight);
                } else {
                    this.biomes.put(biome, this.biomes.get(biome) + weight);
                }
                this.totalweight += weight;
            }
            return this;
        }

        public BiomeGroup addBiome(Biome biome) {
            return this.addBiome(biome, 1.0);
        }

        public Biome getBiome(double value) {
            double weight = MathUtil.clamp(value,0.0,1.0) * this.totalweight;

            double total = 0.0;
            for (Map.Entry<Biome, Double> entry : this.biomes.entrySet()) {
                total += entry.getValue();
                if (total >= weight) {
                    return entry.getKey();
                }
            }

            return null; // shouldn't happen!
        }

        public double getClassificationScore(Biome biome) {
            return 0.0;
        }

        public BiomeGroup setBlobSizeModifier(int size) {
            this.blobSizeModifier = size;
            return this;
        }

        public BiomeGroup setSubBlobSizeModifier(int size) {
            this.subBlobSizeModfier = size;
            return this;
        }
    }

    //------ HeightModEntry Class ---------------------------------------------------------

    public static class HeightModEntry {
        public final IBiomeHeightModifier modifier;
        public final Map<String,Object> arguments;

        public HeightModEntry(IBiomeHeightModifier modifier, Map<String,Object> args) {
            this.modifier = modifier;
            this.arguments = args;
        }
    }
}

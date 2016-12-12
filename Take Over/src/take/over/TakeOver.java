/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package take.over;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author deneir-uy
 */
class Page {

    String label;
    int order;
    int frequency;

    Page(String value, int order, int frequency) {
        this.label = value;
        this.order = order;
        this.frequency = frequency;
    }
}

class IntegerFrequencyComparator implements Comparator<Map.Entry<String, Page>> {

    @Override
    public int compare(Map.Entry<String, Page> x, Map.Entry<String, Page> y) {
        if (x.getValue().frequency != y.getValue().frequency) {
            return x.getValue().frequency - y.getValue().frequency;
        } else {
            return x.getValue().order - y.getValue().order;
        }
    }
}

class IntegerMap<K, V> extends LinkedHashMap<K, V> {

    private final int frames;

    public IntegerMap(int cacheSize, boolean isAccessOrder) {
        super(16, (float) 0.75, isAccessOrder);
        this.frames = cacheSize + 1;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() >= frames;
    }
}

public class TakeOver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String choice, reference;
        int frames;

        do {
            System.out.print("1. FIFO\n2. LFU\n3. LRU\n4. Exit\n\nchoice: ");
            choice = scan.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Reference String: ");
                    reference = scan.nextLine();
                    System.out.print("No. of frames: ");
                    frames = Integer.parseInt(scan.nextLine());
                    fifo(createPages(reference), frames);
                    break;
                case "2":
                    System.out.print("Reference String: ");
                    reference = scan.nextLine();
                    System.out.print("No. of frames: ");
                    frames = Integer.parseInt(scan.nextLine());
                    lfu(LFUCreatePages(reference), frames);
                    break;
                case "3":
                    System.out.print("Reference String: ");
                    reference = scan.nextLine();
                    System.out.print("No. of frames: ");
                    frames = Integer.parseInt(scan.nextLine());
                    lru(createPages(reference), frames);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid input");
                    break;
            }

            System.out.println("");
        } while (true);
    }

    public static void fifo(ArrayList<Integer> pages, int frames) {
        IntegerMap<Integer, Integer> map = new IntegerMap<>(frames, false);
        int pageFaults = 0;
        boolean isFault = false;

        System.out.println("=== FIFO Page Replacement ===");

        for (int i = 0; i < pages.size(); i++) {
            System.out.print("Current page: " + pages.get(i) + ":");

            if (!map.containsKey(pages.get(i))) {
                pageFaults++;
                isFault = true;
            }

            map.put(pages.get(i), pages.get(i));

            System.out.print("" + map.values());

            if (isFault) {
                System.out.println("  X");
            } else {
                System.out.println("   ");
            }
            isFault = false;
        }

        System.out.println("Page faults: " + pageFaults);
    }

    public static void lfu(ArrayList<Page> pages, int frames) {
        HashMap<String, Page> map = new HashMap<>();
        PriorityQueue<Map.Entry<String, Page>> queue = new PriorityQueue<>(new IntegerFrequencyComparator());
        int pageFaults = 0;
        boolean isFault = false;
        System.out.println("=== LFU Page Replacement ===");

        for (int i = 0; i < pages.size(); i++) {
            System.out.print("Current page: " + pages.get(i).label + ":");
            if (!map.containsKey(pages.get(i).label)) {
                pageFaults++;
                isFault = true;

                if (map.size() == frames) {
                    queue.clear();
                    queue.addAll(map.entrySet());
                    map.remove(queue.remove().getKey());
                }
                pages.get(i).order = i;
                map.put(pages.get(i).label, pages.get(i));
                queue.clear();
                queue.addAll(map.entrySet());
            } else {
                map.get(pages.get(i).label).frequency++;
                queue.clear();
                queue.addAll(map.entrySet());
            }
            System.out.print("[");
            for (Iterator<Map.Entry<String, Page>> iterator = queue.iterator(); iterator.hasNext();) {
                Map.Entry<String, Page> next = iterator.next();
                System.out.print(next.getKey());
                if (iterator.hasNext()) {
                    System.out.print(", ");
                }
            }
            System.out.print("]");

            if (isFault) {
                System.out.println("  X");
            } else {
                System.out.println("   ");
            }
            isFault = false;

        }

        System.out.println("Page faults: " + pageFaults);
    }

    public static void lru(ArrayList<Integer> pages, int frames) {
        IntegerMap<Integer, Integer> map = new IntegerMap<>(frames, true);
        int pageFaults = 0;
        boolean isFault = false;

        System.out.println("=== LRU Page Replacement ===");

        for (int i = 0; i < pages.size(); i++) {
            System.out.print("Current page: " + pages.get(i) + ":");

            if (!map.containsKey(pages.get(i))) {
                pageFaults++;
                isFault = true;
            }

            map.put(pages.get(i), pages.get(i));

            System.out.print("" + map.values());

            if (isFault) {
                System.out.println("  X");
            } else {
                System.out.println("   ");
            }
            isFault = false;
        }

        System.out.println("Page faults: " + pageFaults);
    }

    public static ArrayList<Integer> createPages(String reference) {
        ArrayList<Integer> pages = new ArrayList<>();
        String[] splittedReference = reference.split(" ");

        for (String splittedReference1 : splittedReference) {
            pages.add(new Integer(splittedReference1));
        }

        return pages;
    }

    public static ArrayList<Page> LFUCreatePages(String reference) {
        ArrayList<Page> pages = new ArrayList<>();
        String[] splittedReference = reference.split(" ");

        for (String splittedReference1 : splittedReference) {
            pages.add(new Page(splittedReference1, 0, 1));
        }

        return pages;
    }
}

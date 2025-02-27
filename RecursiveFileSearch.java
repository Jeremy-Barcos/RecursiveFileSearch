import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


abstract class FileSystemNode {
    String name;

    public FileSystemNode(String name) {
        this.name = name;
    }

    abstract void display();
}


class FileNode extends FileSystemNode {
    public FileNode(String name) {
        super(name);
    }

    @Override
    void display() {
        System.out.println("File: " + name);
    }
}


class DirectoryNode extends FileSystemNode {
    public DirectoryNode(String name) {
        super(name);
    }

    @Override
    void display() {
        System.out.println("Directory: " + name);
    }
}


interface FileFoundListener {
    void onFileFound(String filePath);
}


class FileSearcher {
    private FileFoundListener listener;

    public FileSearcher(FileFoundListener listener) {
        this.listener = listener;
    }

    public void searchFiles(File directory, String extension, FileWriter writer) throws IOException {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    searchFiles(file, extension, writer); 
                } else if (file.getName().endsWith(extension)) {
                    listener.onFileFound(file.getAbsolutePath());
                    writer.write(file.getAbsolutePath() + "\n");
                }
            }
        }
    }
}

// Main class
public class RecursiveFileSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get user input
        System.out.print("Enter directory path: ");
        String dirPath = scanner.nextLine();
        System.out.print("Enter file extension to search for (e.g., .txt, .java): ");
        String extension = scanner.nextLine();

        File directory = new File(dirPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }

       
        FileFoundListener listener = (filePath) -> System.out.println("File found: " + filePath);

        
        try (FileWriter writer = new FileWriter("search_results.txt")) {
            FileSearcher searcher = new FileSearcher(listener);
            System.out.println("\nSearching...");
            searcher.searchFiles(directory, extension, writer);
            System.out.println("\nSearch completed. Results saved to search_results.txt.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        scanner.close();
    }
}

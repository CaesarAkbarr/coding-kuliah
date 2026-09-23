# Copilot Instructions for AI Coding Agents

## Project Overview
This workspace contains small, self-contained Java programs for educational purposes. Each file implements a distinct system or exercise, with no shared dependencies or complex architecture. There are no build scripts, external libraries, or advanced frameworks in use.

### Key Directories & Files
- `Tugas-Coding-Kuliah/`
  - `responsi.java`: Contains four independent systems (JNE package classification, employee bonus calculator, pallet weighing, cashier system). Each system is implemented as a static method in the `responsi` class and invoked from `main()`.
  - `belajarLoop.java`, `test.java`: Standalone Java exercises, likely focused on basic programming concepts.
- `calculator/Calculator.java`: Another standalone Java file, likely implementing calculator logic.

## Developer Workflows
- **Build & Run**: Compile and run individual `.java` files using standard Java commands. Example:
  ```powershell
  javac responsi.java; java responsi
  ```
  No build tools (Maven/Gradle) or test frameworks are present.
- **Debugging**: Use IDE or command-line Java debugging tools. No custom debug scripts.

## Project-Specific Patterns
- Each file is a self-contained program. There is no cross-file communication or shared state.
- All logic is implemented in static methods within a single class per file.
- User input is handled via `Scanner` and output via `System.out.println`.
- Resource management: Each method creates and closes its own `Scanner` instance. Be careful to avoid closing `System.in` multiple times if refactoring.
- Variable naming and prompts are in Indonesian, reflecting the educational context.

## Integration Points
- No external dependencies, APIs, or integration points.
- No configuration files, environment variables, or external resources required.

## Conventions & Recommendations
- Maintain the single-class, static-method structure for new exercises.
- Keep user prompts and output consistent with existing Indonesian phrasing.
- When adding new systems, implement them as additional static methods and invoke from `main()`.
- Avoid introducing frameworks or build tools unless the project structure changes.

## Commenting Style for AI-Generated Code
When generating code (especially Java), AI agents must include Indonesian-language comments that explain the purpose of each logical block, not every single line. Follow this style:
- Use block-level comments above related lines of code
- Keep comments concise, clear, and in Indonesian
- Do not modify the code itself
- Do not comment every line—group related lines and explain their purpose
- Use this format:
// Sistem untuk menghitung bonus karyawan
public static void gajiPokok () {
    
    // Variabel input dan inisialisasi
    Scanner input = new Scanner(System.in);
    double gaji, bonus = 0, total = 0;
    int karyawan;

    // Input jumlah karyawan
    System.out.print("Masukkan jumlah karyawan: ");
    karyawan = input.nextInt();

    // Hitung dan tampilkan bonus tiap karyawan
    for (int i = 1 ; i <= karyawan ; i++) {
        System.out.print("Masukkan gaji pokok karyawan ke-" + i + ": ");
        gaji = input.nextDouble();
        bonus = gaji * 0.25;
        System.out.println("Bonus karyawan ke-" + i + ": " + bonus);
        total += bonus;
    }

    // Tampilkan total bonus
    System.out.println("Total bonus yang harus dibayarkan: " + total );
}

Copilot agents must follow this commenting style when generating or completing Java code in this project.

## Example: Adding a New System
To add a new exercise (e.g., a temperature converter):
1. Implement as a new static method in `responsi.java`:
   ```java
   public static void konversiSuhu() {
       // ... logic ...
   }
   ```
2. Call the method from `main()`:
   ```java
   public static void main(String[] args) {
       // ...existing calls...
       konversiSuhu();
   }
   ```


---


For questions or unclear conventions, ask for clarification or examples from the user.

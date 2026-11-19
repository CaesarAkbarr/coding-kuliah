import re
from pathlib import Path

COURSE_NAMES = {
    "PAVBW": "Pemrograman Aplikasi Visual Berbasis Web",
    "PBO": "Pemrograman Berorientasi Objek",
    "PWP": "Pemrograman Web Praktikum",
    "RPL": "Rekayasa Perangkat Lunak",
    "SO": "Sistem Operasi",
}

START_MARKER = "<!-- AUTO_SEMESTER_3_START -->"
END_MARKER = "<!-- AUTO_SEMESTER_3_END -->"


def build_semester3_table(sem3_dir: Path) -> str:
    rows = [
        "| Mata Kuliah | Kode | Daftar Pertemuan & Proyek |",
        "| :--- | :---: | :--- |",
    ]
    if not sem3_dir.exists():
        return "\n".join(rows)

    courses = sorted([p for p in sem3_dir.iterdir() if p.is_dir()], key=lambda p: p.name.lower())
    for course in courses:
        code = course.name
        full_name = COURSE_NAMES.get(code, code)
        items = sorted(
            [item.name for item in course.iterdir() if not item.name.startswith(".")],
            key=str.lower,
        )
        items_str = ", ".join(f"`{item}`" for item in items) if items else "_Belum ada isi_"
        rows.append(f"| {full_name} | `{code}` | {items_str} |")

    return "\n".join(rows)


def main() -> None:
    repo_root = Path(__file__).resolve().parents[2]
    readme_path = repo_root / "README.md"
    sem3_dir = repo_root / "Semester 3"

    content = readme_path.read_text(encoding="utf-8")
    pattern = re.compile(
        rf"{re.escape(START_MARKER)}.*?{re.escape(END_MARKER)}",
        re.DOTALL,
    )
    table = build_semester3_table(sem3_dir)
    replacement = f"{START_MARKER}\n{table}\n{END_MARKER}"

    if not pattern.search(content):
        raise ValueError("Markers AUTO_SEMESTER_3_START / END not found in README.md")

    updated = pattern.sub(replacement, content)
    if updated != content:
        readme_path.write_text(updated, encoding="utf-8")
        print("README.md updated.")
    else:
        print("README.md already up to date.")


if __name__ == "__main__":
    main()

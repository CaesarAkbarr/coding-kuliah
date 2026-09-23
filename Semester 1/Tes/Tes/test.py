import os
from datetime import datetime
import matplotlib.pyplot as plt
import ipywidgets as widgets
from IPython.display import display, clear_output

def apply_filters():
    dff = df.copy()
    
    if w_dept.value != "All":
        dff = dff[dff["Department"] == w_dept.value]
        
    if w_status.value != "All":
        dff = dff[dff["Status"] == w_status.value]
        
    if w_loc.value != "All":
        dff = dff[dff["Location"] == w_loc.value]
        
    if w_gender.value != "All":
        dff = dff[dff["Gender"] == w_gender.value]
        
    if w_session.value != "All":
        dff = dff[dff["Session"] == w_session.value]
        
    # salary range
    dff = dff[(dff["Salary"] >= w_min_salary.value) & (dff["Salary"] <= w_max_salary.value)]
    
    # search nama
    if w_name.value.strip():
        dff = dff[dff["Name"].str.contains(w_name.value.strip(), case=False, na=False)]
        
    return dff

def render_dashboard(_=None):
    with out:
        clear_output(wait=True)
        dff = apply_filters()
        
        # METRICS
        total = len(dff)
        avg_salary = dff["Salary"].mean()
        med_salary = dff["Salary"].median()
        avg_perf = dff["Performance_Score"].mean()
        avg_exp = dff["Experience"].mean()
        
        print("=== Employee Dashboard (Jupyter App) ===")
        print(f"Total (hasil filter): {total}")
        print(f"Avg Salary: {avg_salary:,.2f}" if total else "Avg Salary: -")
        print(f"Median Salary: {med_salary:,.2f}" if total else "Median Salary: -")
        print(f"Avg Performance Score: {avg_perf:,.2f}" if total else "Avg Performance Score: -")
        print(f"Avg Experience: {avg_exp:,.2f}" if total else "Avg Experience: -")
        print("-" * 70)
        
        # TABLE
        display(dff.head(30))
        
        # CHARTS
        if total > 0:
            # Chart 1: count per department
            plt.figure()
            dff["Department"].value_counts().plot(kind="bar")
            plt.title("Jumlah Karyawan per Department")
            plt.xlabel("Department")
            plt.ylabel("Count")
            plt.show()
            
            # Chart 2: salary distribution (sorted line chart)
            plt.figure()
            dff["Salary"].dropna().sort_values().reset_index(drop=True).plot()
            plt.title("Distribusi Salary (sorted)")
            plt.xlabel("Index (urut)")
            plt.ylabel("Salary")
            plt.show()
            
            # Chart 3: status pie (opsional)
            plt.figure()
            dff["Status"].value_counts().plot(kind="pie", autopct="%1.1f%%")
            plt.title("Komposisi Status")
            plt.ylabel("")
            plt.show()

def export_csv(_=None):
    dff = apply_filters()
    ts = datetime.now().strftime("%Y%m%d_%H%M%S")
    out_name = f"filtered_employee_{ts}.csv"
    dff.to_csv(out_name, index=False)
    
    with out:
        print(f"File export berhasil: {out_name}")
        print(f"Lokasi: {os.path.abspath(out_name)}")

btn_export.on_click(export_csv)

# auto update ketika widget berubah
for w in [w_dept, w_status, w_loc, w_gender, w_session, w_name, w_min_salary, w_max_salary]:
    w.observe(render_dashboard, names="value")

ui_row1 = widgets.HBox([w_dept, w_status, w_loc])
ui_row2 = widgets.HBox([w_gender, w_session])
ui_row3 = widgets.HBox([w_name])
ui_row4 = widgets.HBox([w_min_salary, w_max_salary])
ui_row5 = widgets.HBox([btn_export])

display(ui_row1, ui_row2, ui_row3, ui_row4, ui_row5, out)
render_dashboard()
using System;
using System.Windows.Forms;
using System.Threading;
using System.Collections.Generic;

namespace ThesesController.GUI
{
    public partial class MainForm : Form
    {
        Model cnt;
        bool initCompeted = false;

        public MainForm(Model cnt)
        {
            this.cnt = cnt;

            InitializeComponent();

            this.InitComboBoxes();
            this.initCompeted = true;


            //Thread dataGridsInit = new Thread(this.InitDataGridsSh);
            //dataGridsInit.Start();
            this.InitDataGrids();

            //import tab hide
            this.tabControll_Main.TabPages.RemoveAt(3);

        }

        private void InitComboBoxes()
        {


            var query = cnt.GetFaculties();
            this.comboBox_T1_Fac.DataSource = query;
            this.comboBox_T1_Fac.DisplayMember = "NAME";
            this.comboBox_T1_Fac.ValueMember = "NAME";
            this.comboBox_T1_Fac.DropDownStyle = ComboBoxStyle.DropDownList;


            query = cnt.GetFaculties();
            this.comboBox_T2_SFac.DataSource = query;
            this.comboBox_T2_SFac.DisplayMember = "NAME";
            this.comboBox_T2_SFac.ValueMember = "NAME";
            this.comboBox_T2_SFac.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetFaculties();
            this.comboBox_T2_AFac.DataSource = query;
            this.comboBox_T2_AFac.DisplayMember = "NAME";
            this.comboBox_T2_AFac.ValueMember = "ID";
            this.comboBox_T2_AFac.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetFaculties();
            query.RemoveAt(0);
            this.comboBox_T3_Fac1.DataSource = query;
            this.comboBox_T3_Fac1.DisplayMember = "NAME";
            this.comboBox_T3_Fac1.ValueMember = "ID";
            this.comboBox_T3_Fac1.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetDepartaments();
            this.comboBox_T1_Dep.DataSource = query;
            this.comboBox_T1_Dep.DisplayMember = "NAME";
            this.comboBox_T1_Dep.ValueMember = "NAME";
            this.comboBox_T1_Dep.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetDepartaments();
            this.comboBox_T2_ADep.DataSource = query;
            this.comboBox_T2_ADep.DisplayMember = "NAME";
            this.comboBox_T2_ADep.ValueMember = "ID";
            this.comboBox_T2_ADep.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetDepartaments();
            this.comboBox_T2_SDep.DataSource = query;
            this.comboBox_T2_SDep.DisplayMember = "NAME";
            this.comboBox_T2_SDep.ValueMember = "NAME";
            this.comboBox_T2_SDep.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetDepartaments();
            query.RemoveAt(0);
            this.comboBox_T3_Dep.DataSource = query;
            this.comboBox_T3_Dep.DisplayMember = "NAME";
            this.comboBox_T3_Dep.ValueMember = "ID";
            this.comboBox_T3_Dep.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetDegrees();
            this.comboBox_T1_Deg.DataSource = query;
            this.comboBox_T1_Deg.DisplayMember = "NAME";
            this.comboBox_T1_Deg.ValueMember = "NAME";
            this.comboBox_T1_Deg.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetDegrees();
            query.RemoveAt(0);
            this.comboBox_T3_Deg.DataSource = query;
            this.comboBox_T3_Deg.DisplayMember = "NAME";
            this.comboBox_T3_Deg.ValueMember = "ID";
            this.comboBox_T3_Deg.DropDownStyle = ComboBoxStyle.DropDownList;


            query = cnt.GetPositions();
            this.comboBox_T2_APos.DataSource = query;
            this.comboBox_T2_APos.DisplayMember = "NAME";
            this.comboBox_T2_APos.ValueMember = "ID";
            this.comboBox_T2_APos.DropDownStyle = ComboBoxStyle.DropDownList;


            query = cnt.GetSpecialities();
            this.comboBox_T1_Spec.DataSource = query;
            this.comboBox_T1_Spec.DisplayMember = "NAME";
            this.comboBox_T1_Spec.ValueMember = "NAME";
            this.comboBox_T1_Spec.DropDownStyle = ComboBoxStyle.DropDownList;


            query = cnt.GetOfficials();
            this.comboBox_T1_Ofc.DataSource = query;
            this.comboBox_T1_Ofc.DisplayMember = "NAME";
            this.comboBox_T1_Ofc.ValueMember = "NAME";
            this.comboBox_T1_Ofc.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetOfficials();
            this.comboBox_T3_Head1.DataSource = query;
            this.comboBox_T3_Head1.DisplayMember = "NAME";
            this.comboBox_T3_Head1.ValueMember = "ID";
            this.comboBox_T3_Head1.DropDownStyle = ComboBoxStyle.DropDownList;

            query = cnt.GetOfficials();
            this.comboBox_T3_Head2.DataSource = query;
            this.comboBox_T3_Head2.DisplayMember = "NAME";
            this.comboBox_T3_Head2.ValueMember = "ID";
            this.comboBox_T3_Head2.DropDownStyle = ComboBoxStyle.DropDownList;

            //Score
            List<DropDownType> scrs = new List<DropDownType>();
            scrs.Add(new DropDownType { ID = 0, NAME = "===" });
            scrs.Add(new DropDownType { ID = 1, NAME = "N/A" });
            scrs.Add(new DropDownType { ID = 3, NAME = "3" });
            scrs.Add(new DropDownType { ID = 4, NAME = "4" });
            scrs.Add(new DropDownType { ID = 5, NAME = "5" });
            scrs.Add(new DropDownType { ID = 6, NAME = "6" });

            this.comboBox_T1_Scr.DataSource = scrs;
            this.comboBox_T1_Scr.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_T1_Scr.DisplayMember = "NAME";
            this.comboBox_T1_Scr.ValueMember = "ID";

        }

        private void InitDataGrids()
        {

            this.refilterThesises();
            this.dataGridView_T1_Thesises.Columns["ID"].Visible = false;
            this.dataGridView_T1_Thesises.Columns["DT_DEF"].Visible = false;
            this.dataGridView_T1_Thesises.RowHeadersVisible = false;
            this.dataGridView_T1_Thesises.MultiSelect = false;
            this.dataGridView_T1_Thesises.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            this.dataGridView_T1_Thesises.Columns["TITLE"].Width = 250;
            // this.dataGridView1.ColumnHeadersVisible = false;
            // this.dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
           
            //bg translations
            this.dataGridView_T1_Thesises.Columns["TITLE"].HeaderText = "Тема";
            this.dataGridView_T1_Thesises.Columns["AUTHOR"].HeaderText = "Автор";
            this.dataGridView_T1_Thesises.Columns["ADVISOR"].HeaderText = "Ръководител";
            this.dataGridView_T1_Thesises.Columns["YEAR"].HeaderText = "Год. Възл.";
            this.dataGridView_T1_Thesises.Columns["SCORE"].HeaderText = "Оценка";
            this.dataGridView_T1_Thesises.Columns["DEGREE"].HeaderText = "ОКС";
            this.dataGridView_T1_Thesises.Columns["FACULTY"].HeaderText = "Факултет";
            this.dataGridView_T1_Thesises.Columns["DEPARTAMENT"].HeaderText = "Катедра";
            this.dataGridView_T1_Thesises.Columns["SPECIALITY"].HeaderText = "Специалност";



            this.refilterOfficials();
            this.dataGridView_T2_Opr.Columns["ID"].Visible = false;
            this.dataGridView_T2_Opr.Columns["POSITION"].Visible = false;
            this.dataGridView_T2_Opr.Columns["FNAME"].Visible = false;
            this.dataGridView_T2_Opr.Columns["LNAME"].Visible = false;
            this.dataGridView_T2_Opr.Columns["EMAIL"].Visible = false;
            this.dataGridView_T2_Opr.RowHeadersVisible = false;
            this.dataGridView_T2_Opr.MultiSelect = false;
            this.dataGridView_T2_Opr.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            this.dataGridView_T2_Opr.Columns[1].Width = 250;
            this.dataGridView_T2_Opr.Columns[2].Width = 250;
            this.dataGridView_T2_Opr.Columns[3].Width = 250;
            this.dataGridView_T2_Opr.Columns["NAME"].HeaderText = "Име";
            this.dataGridView_T2_Opr.Columns["FACULTY"].HeaderText = "Факултет";
            this.dataGridView_T2_Opr.Columns["DEPARTAMENT"].HeaderText = "Катедра";

            var queryFac = cnt.GetFacultiesDG();
            this.dataGridView_T3_Fac.DataSource = queryFac;
            this.dataGridView_T3_Fac.Columns["ID"].Visible = false;
            this.dataGridView_T3_Fac.Columns["HEAD_ID"].Visible = false;
            this.dataGridView_T3_Fac.Columns["NAME"].Frozen = false;
            //this.dataGridView3.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridView_T3_Fac.RowHeadersVisible = false;
            //this.dataGridView3.ColumnHeadersVisible = false;
            dataGridView_T3_Fac.Columns[1].Width = 300;
            this.dataGridView_T3_Fac.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            this.dataGridView_T3_Fac.Columns["NAME"].HeaderText = "Име";


            var queryDep = cnt.GetDepartamentsDG();
            this.dataGridView_T3_Dep.DataSource = queryDep;
            this.dataGridView_T3_Dep.Columns["ID"].Visible = false;
            this.dataGridView_T3_Dep.Columns["HEAD_ID"].Visible = false;
            // this.dataGridView4.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridView_T3_Dep.RowHeadersVisible = false;
            dataGridView_T3_Dep.Columns[1].Width = 200;
            dataGridView_T3_Dep.Columns[2].Width = 200;
            this.dataGridView_T3_Dep.MultiSelect = false;
            this.dataGridView_T3_Dep.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            this.dataGridView_T3_Dep.Columns["NAME"].HeaderText = "Име";
            this.dataGridView_T3_Dep.Columns["FACULTY"].HeaderText = "Факултет";

            var querySpec = cnt.GetSpecialitiesDG();
            this.dataGridView_T3_Spec.DataSource = querySpec;
            this.dataGridView_T3_Spec.Columns["ID"].Visible = false;
            //this.dataGridView5.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            this.dataGridView_T3_Spec.RowHeadersVisible = false;
            this.dataGridView_T3_Spec.Columns[1].Width = 200;
            this.dataGridView_T3_Spec.Columns[2].Width = 200;
            this.dataGridView_T3_Spec.MultiSelect = false;
            this.dataGridView_T3_Spec.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            this.dataGridView_T3_Spec.Columns["NAME"].HeaderText = "Име";
            this.dataGridView_T3_Spec.Columns["DEGREE"].HeaderText = "ОКС";
            this.dataGridView_T3_Spec.Columns["DEPARTAMENT"].HeaderText = "Катедра";

        }

        private void refilterThesises()
        {

            if (!this.initCompeted)
                return;

            string faculty = null, departament = null,
                speciality = null, degree = null,
                advisor = null,
                author = null,
                title = null;
            int score;


            faculty = (string)this.comboBox_T1_Fac.SelectedValue;
            departament = (string)this.comboBox_T1_Dep.SelectedValue;
            speciality = (string)this.comboBox_T1_Spec.SelectedValue;
            advisor = (string)this.comboBox_T1_Ofc.SelectedValue;
            degree = (string)this.comboBox_T1_Deg.SelectedValue;

            author = this.textBox_T1_Stdnt.Text;

            title = this.textBox_T1_Ttl.Text;

            score = (int)this.comboBox_T1_Scr.SelectedValue;

            var res = cnt.GetThesisesDG(title, author, advisor, faculty, departament, speciality, degree, score);
            this.dataGridView_T1_Thesises.DataSource = res;

            //object obj;

            //obj = comboBox_T1_Fac.SelectedValue;

            //if (obj != null && obj.GetType() == typeof(string))
            //    faculty = (string)obj;

            //obj = this.comboBox_T1_Dep.SelectedValue;
            // if (obj != null && obj.GetType() == typeof(string))
            //departament = (string)obj;

            //obj = this.comboBox_T1_Spec.SelectedValue;
            //if (obj != null && obj.GetType() == typeof(string))
            //    speciality = (string)obj;

            // obj = this.comboBox_T1_Ofc.SelectedValue;
            //if (obj != null && obj.GetType() == typeof(string))
            //    advisor = (string)obj;

            //  obj = this.comboBox_T1_Deg.SelectedValue;
            //if (obj != null && obj.GetType() == typeof(string))
            //    degree = (string)obj;

        }

        ////Cross thread code to access thesies data grid view
        //delegate void SetTableContent(List<ThesiesTableContentType> src);

        //private void SetThesisesContent(List<ThesiesTableContentType> src)
        //{

        //    if (this.dataGridView_T1_Thesises.InvokeRequired)
        //    {
        //        SetTableContent d = new SetTableContent(SetThesisesContent);
        //        this.Invoke(d, new object[] { src });
        //    }
        //    else
        //    {
        //        this.dataGridView_T1_Thesises.DataSource = src;
        //    }
        //}


        private void refilterOfficials()
        {

            if (!this.initCompeted)
                return;

            string faculty = null, departament = null, name = null;

            faculty = (string)this.comboBox_T2_SFac.SelectedValue;
            departament = (string)this.comboBox_T2_SDep.SelectedValue;
            name = this.textBox_T2_SName.Text;

            var res = cnt.GetOfficialsDG(name, faculty, departament);
            this.dataGridView_T2_Opr.DataSource = res;

        }

        private void comboBox_T1_Fac_SelectedIndexChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void comboBox_T1_Spec_SelectedIndexChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void comboBox_T1_Dep_SelectedIndexChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void comboBox_T1_Deg_SelectedIndexChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void textBox_T1_Stdnt_TextChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void textBox_T1_Ttl_TextChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void comboBox_T1_Ofc_SelectedIndexChanged(object sender, EventArgs e)
        {
            refilterThesises();
        }

        private void textBox_T2_SName_TextChanged(object sender, EventArgs e)
        {
            this.refilterOfficials();
        }

        private void comboBox_T2_SFac_SelectedIndexChanged(object sender, EventArgs e)
        {
            this.refilterOfficials();

            //   if (this.dataGridView_T1_Thesises.RowCount == 0)
            //     this.panel2.Enabled = false;
        }

        private void comboBox_T2_SDep_SelectedIndexChanged(object sender, EventArgs e)
        {
            this.refilterOfficials();
        }

        ////hack anonymos type
        //private static T AnonymousTypeCast<T>(object anonymous, T typeExpression)
        //{
        //    return (T)anonymous;
        //}

        private void dataGridView_T2_Opr_SelectionChanged(object sender, EventArgs e)
        {

            if (dataGridView_T2_Opr.Rows.GetRowCount(DataGridViewElementStates.Selected) > 0)
            {
                this.panel1.Enabled = true;

                this.textBox_T2_AFname.Text = (string)this.dataGridView_T2_Opr.SelectedRows[0].Cells["FNAME"].Value;
                this.textBox_T2_ALname.Text = (string)this.dataGridView_T2_Opr.SelectedRows[0].Cells["LNAME"].Value;
                this.textBox_T2_Aeml.Text = (string)this.dataGridView_T2_Opr.SelectedRows[0].Cells["EMAIL"].Value;
                this.comboBox_T2_ADep.Text = (string)this.dataGridView_T2_Opr.SelectedRows[0].Cells["DEPARTAMENT"].Value;
                this.comboBox_T2_AFac.Text = (string)this.dataGridView_T2_Opr.SelectedRows[0].Cells["FACULTY"].Value;
                this.comboBox_T2_APos.Text = (string)this.dataGridView_T2_Opr.SelectedRows[0].Cells["POSITION"].Value;

            }
            else if (!this.panel1.Focused)
            {
                this.panel1.Enabled = false;
            }

        }

        private void button_T2_save_Click(object sender, EventArgs e)
        {

            int selectedIdnex = 0, id;

            if (dataGridView_T2_Opr.Rows.GetRowCount(DataGridViewElementStates.Selected) == 0)
            {
                //insert
                this.cnt.InsertOfficial(
                 (int)this.comboBox_T2_ADep.SelectedValue,
                 (int)this.comboBox_T2_APos.SelectedValue,
                 this.textBox_T2_AFname.Text,
                 this.textBox_T2_ALname.Text,
                 this.textBox_T2_Aeml.Text);

            }
            else
            {
                id = (int)this.dataGridView_T2_Opr.SelectedRows[0].Cells["ID"].Value;
                selectedIdnex = this.dataGridView_T2_Opr.SelectedRows[0].Index;

                //update
                this.cnt.UpdateOfficial(id,
                    (int)this.comboBox_T2_ADep.SelectedValue,
                    (int)this.comboBox_T2_APos.SelectedValue,
                    this.textBox_T2_AFname.Text,
                    this.textBox_T2_ALname.Text,
                    this.textBox_T2_Aeml.Text);
            }

            //reload
            this.refilterOfficials();

            //fix selection
            if (selectedIdnex == 0)
            {
                this.dataGridView_T2_Opr.ClearSelection();
                this.panel1.Enabled = false;
            }
            else
                this.dataGridView_T2_Opr.Rows[selectedIdnex].Selected = true;

        }

        private void comboBox_T2_AFac_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (!this.initCompeted)
                return;

            //filter departament
            var query = cnt.GetDepartaments((int)this.comboBox_T2_AFac.SelectedValue);
            this.comboBox_T2_ADep.DataSource = query;

        }

        private void button_T2_New_Click(object sender, EventArgs e)
        {

            this.panel1.Enabled = true;
            this.panel1.Focus();
            this.dataGridView_T2_Opr.ClearSelection();

            this.comboBox_T2_APos.SelectedValue = 0;
            this.comboBox_T2_AFac.SelectedValue = 0;
            this.textBox_T2_AFname.Text = "";
            this.textBox_T2_ALname.Text = "";
            this.textBox_T2_Aeml.Text = "";

        }

        private void dataGridView_T3_Dep_SelectionChanged(object sender, EventArgs e)
        {
            if (this.dataGridView_T3_Dep.Rows.GetRowCount(DataGridViewElementStates.Selected) > 0)
            {

                this.panel3.Enabled = true;
                this.textBox_T3_Dep.Text = (string)this.dataGridView_T3_Dep.SelectedRows[0].Cells["NAME"].Value;
                this.comboBox_T3_Fac1.Text = (string)this.dataGridView_T3_Dep.SelectedRows[0].Cells["FACULTY"].Value;


                var head = (int?)this.dataGridView_T3_Dep.SelectedRows[0].Cells["HEAD_ID"].Value;
                if (head == null)
                    head = 0;

                this.comboBox_T3_Head2.SelectedValue = head;
            }
        }

        private void button_T3_NewDep_Click(object sender, EventArgs e)
        {

            this.dataGridView_T3_Dep.ClearSelection();

            this.comboBox_T3_Fac1.SelectedValue = 0;
            this.comboBox_T3_Head2.SelectedValue = 0;
            this.textBox_T3_Dep.Text = "";


        }

        private void button_T3_SaveDep_Click(object sender, EventArgs e)
        {

            int selectedIdnex = -1, id;

            if (this.dataGridView_T3_Dep.Rows.GetRowCount(DataGridViewElementStates.Selected) == 0)
            {
                //insert
                this.cnt.InsertDepartament(this.textBox_T3_Dep.Text,
                    (int)this.comboBox_T3_Fac1.SelectedValue,
                  (int?)this.comboBox_T3_Head1.SelectedValue);

            }
            else
            {
                id = (int)this.dataGridView_T3_Dep.SelectedRows[0].Cells["ID"].Value;
                selectedIdnex = this.dataGridView_T3_Dep.SelectedRows[0].Index;

                //update
                this.cnt.UpdateDepartament(
                    id,
                    this.textBox_T3_Dep.Text,
                    (int)this.comboBox_T3_Fac1.SelectedValue,
                  (int?)this.comboBox_T3_Head2.SelectedValue);
            }

            var query = cnt.GetDepartamentsDG();
            this.dataGridView_T3_Dep.DataSource = query;

            if (selectedIdnex == -1)
            {
                this.dataGridView_T3_Dep.ClearSelection();
                this.panel3.Enabled = false;
            }
            else
                this.dataGridView_T3_Dep.Rows[selectedIdnex].Selected = true;

        }

        private void dataGridView_T3_Fac_SelectionChanged(object sender, EventArgs e)
        {

            if (this.dataGridView_T3_Fac.Rows.GetRowCount(DataGridViewElementStates.Selected) > 0)
            {

                this.panel2.Enabled = true;
                this.textBox_T3_Fac.Text = (string)this.dataGridView_T3_Fac.SelectedRows[0].Cells["NAME"].Value;

                var head = (int?)this.dataGridView_T3_Fac.SelectedRows[0].Cells["HEAD_ID"].Value;
                if (head == null)
                    head = 0;

                this.comboBox_T3_Head1.SelectedValue = head;
            }

        }

        private void button_T3_NewFac_Click(object sender, EventArgs e)
        {
            this.dataGridView_T3_Fac.ClearSelection();

            this.comboBox_T3_Head1.SelectedValue = 0;
            this.textBox_T3_Fac.Text = "";
        }

        private void button_T3_SaveFac_Click(object sender, EventArgs e)
        {
            int selectedIdnex = -1, id;

            if (this.dataGridView_T3_Fac.Rows.GetRowCount(DataGridViewElementStates.Selected) == 0)
            {

                //insert
                this.cnt.InsertFaculty(this.textBox_T3_Dep.Text,
                  (int?)this.comboBox_T3_Head1.SelectedValue);

            }
            else
            {
                id = (int)this.dataGridView_T3_Fac.SelectedRows[0].Cells["ID"].Value;
                selectedIdnex = this.dataGridView_T3_Fac.SelectedRows[0].Index;

                //update
                this.cnt.UpdateFaculty(
                    id,
                    this.textBox_T3_Fac.Text,
                  (int?)this.comboBox_T3_Head1.SelectedValue);
            }

            var query = cnt.GetFacultiesDG();
            this.dataGridView_T3_Fac.DataSource = query;

            if (selectedIdnex == -1)
            {
                this.dataGridView_T3_Fac.ClearSelection();
                this.panel2.Enabled = false;
            }
            else
                this.dataGridView_T3_Fac.Rows[selectedIdnex].Selected = true;

        }

        private void dataGridView_T3_Spec_SelectionChanged(object sender, EventArgs e)
        {


            if (this.dataGridView_T3_Spec.Rows.GetRowCount(DataGridViewElementStates.Selected) > 0)
            {

                this.panel4.Enabled = true;
                this.textBox_T3_Spec.Text = (string)this.dataGridView_T3_Spec.SelectedRows[0].Cells["NAME"].Value;
                this.comboBox_T3_Deg.Text = (string)this.dataGridView_T3_Spec.SelectedRows[0].Cells["DEGREE"].Value;
                this.comboBox_T3_Dep.Text = (string)this.dataGridView_T3_Spec.SelectedRows[0].Cells["DEPARTAMENT"].Value;

            }

        }

        private void button_T3_NewSpec_Click(object sender, EventArgs e)
        {

            this.dataGridView_T3_Spec.ClearSelection();

            this.comboBox_T3_Dep.SelectedValue = 0;
            this.comboBox_T3_Deg.SelectedValue = 0;
            this.textBox_T3_Spec.Text = "";

        }

        private void button_T3_SaveSpec_Click(object sender, EventArgs e)
        {


            int selectedIdnex = -1, id;

            if (this.dataGridView_T3_Spec.Rows.GetRowCount(DataGridViewElementStates.Selected) == 0)
            {

                //insert
                this.cnt.InsertSpeciality(this.textBox_T3_Spec.Text,
                    (int)this.comboBox_T3_Dep.SelectedValue,
                  (int)this.comboBox_T3_Deg.SelectedValue);

            }
            else
            {
                id = (int)this.dataGridView_T3_Spec.SelectedRows[0].Cells["ID"].Value;
                selectedIdnex = this.dataGridView_T3_Spec.SelectedRows[0].Index;

                //update
                this.cnt.UpdateSpeciality(id,
                    this.textBox_T3_Spec.Text,
                (int)this.comboBox_T3_Dep.SelectedValue,
              (int)this.comboBox_T3_Deg.SelectedValue);

            }

            var query = cnt.GetSpecialitiesDG();
            this.dataGridView_T3_Spec.DataSource = query;

            if (selectedIdnex == -1)
            {
                this.dataGridView_T3_Spec.ClearSelection();
                this.panel4.Enabled = false;
            }
            else
                this.dataGridView_T3_Spec.Rows[selectedIdnex].Selected = true;

        }

        private void dataGridView_T1_Thesises_CellClick(object sender, DataGridViewCellEventArgs e)
        {

            int col = this.dataGridView_T1_Thesises.CurrentCell.ColumnIndex;
            int row = this.dataGridView_T1_Thesises.CurrentCell.RowIndex;
            int id = (int)this.dataGridView_T1_Thesises.Rows[row].Cells["ID"].Value;

            // MessageBox.Show("You clicked row: " + row + ", column: " + col + ", id: " + id, "Info", MessageBoxButtons.OK, MessageBoxIcon.Information);

            switch (col)
            {
                case 0:
                    AddEditThesisForm aetw = new AddEditThesisForm(this.cnt, id);
                    aetw.ShowDialog();
                    this.refilterThesises();
                    this.dataGridView_T1_Thesises.Rows[row].Selected = true;
                    break;
                case 1:
                    FilesForm ffw = new FilesForm(this.cnt, id);
                    ffw.ShowDialog();

                    break;
                default:
                    //do nothing
                    break;
            }

        }

        private void button_T1_New_Click(object sender, EventArgs e)
        {
            AddEditThesisForm aetw = new AddEditThesisForm(this.cnt);
            aetw.ShowDialog();
            this.refilterThesises();
        }

        private void comboBox_T1_Scr_SelectedIndexChanged(object sender, EventArgs e)
        {
            this.refilterThesises();
        }

        private void dataGridView_T1_Thesises_CellDoubleClick(object sender, DataGridViewCellEventArgs e)
        {
            int row = this.dataGridView_T1_Thesises.CurrentCell.RowIndex;
            int id = (int)this.dataGridView_T1_Thesises.Rows[row].Cells["ID"].Value;

            AddEditThesisForm aetw = new AddEditThesisForm(this.cnt, id);
            aetw.ShowDialog();
            this.refilterThesises();
            this.dataGridView_T1_Thesises.Rows[row].Selected = true;


        }

        private void translateToEnglish()
        {
            this.label9.Text = "Degree:";
            this.label8.Text = "Score:";
            this.label7.Text = "Year:";
            this.label6.Text = "Speciality:";
            this.label5.Text = "Departament:";
            this.label4.Text = "Faculty:";
            this.label3.Text = "Advisor:";
            this.label2.Text = "Title:";
            this.label1.Text = "Author:";
            this.label26.Text = "Faculty:";
            this.label17.Text = "E-mail:";
            this.label16.Text = "Departament:";
            this.label15.Text = "Position:";
            this.label14.Text = "Last Name:";
            this.label13.Text = "First Name:";
            this.label11.Text = "Departament:";
            this.label12.Text = "Faculty:";
            this.label10.Text = "Name:";
            this.label29.Text = "Specialities:";
            this.label28.Text = "Departaments:";
            this.label27.Text = "Faculties:";
            this.label25.Text = "Degree:";
            this.label24.Text = "Departament:";
            this.label23.Text = "Speciality:";
            this.label20.Text = "Departament:";
            this.label22.Text = "Head:";
            this.label19.Text = "Faculty:";
            this.label21.Text = "Head:";
            this.label18.Text = "Faculty:";
            this.button_T1_New.Text = "New";
            this.button_T2_save.Text = "Save";
            this.button_T2_New.Text = "New";
            this.button_T3_SaveSpec.Text = "Save";
            this.button_T3_SaveDep.Text = "Save";
            this.button_T3_SaveFac.Text = "Save";
            this.button_T3_NewSpec.Text = "New";
            this.button_T3_NewFac.Text = "New";
            this.button_T3_NewDep.Text = "New";
        }

        private void panel3_Paint(object sender, PaintEventArgs e)
        {

        }

        private void label29_Click(object sender, EventArgs e)
        {

        }

        private void dataGridView_T3_Spec_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void panel4_Paint(object sender, PaintEventArgs e)
        {

        }

        private void tabPage3_Click(object sender, EventArgs e)
        {

        }

        private void label22_Click(object sender, EventArgs e)
        {

        }

    }
}

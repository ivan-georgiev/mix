using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace ThesesController.GUI
{
    public partial class AddEditThesisForm : Form
    {
        Model cnt;
        int id = 0;

        Boolean initFinished = false;

        public AddEditThesisForm(Model cnt, int id)
        {
            this.id = id;
            this.cnt = cnt;

            InitializeComponent();
            modifyLook();
            this.Text = "Modify Thesis";
            initFinished = true;


            //load fields 
            this.textBox_FName.Text = this.cnt.GetFName(id).TrimEnd();
            this.textBox_SName.Text = this.cnt.GetSName(id);
            this.textBox_LName.Text = this.cnt.GetLName(id).TrimEnd();
            this.textBox_Title.Text = this.cnt.GetTitle(id).TrimEnd();
            this.textBox_FacN.Text = this.cnt.GetFacNmb(id).TrimEnd();
            this.textBox_Email.Text = this.cnt.GetEmail(id);
            this.richTextBox_Annot.Text = this.cnt.GetAnnot(id).TrimEnd();
            this.comboBox_Country.SelectedValue = this.cnt.GetCountryId(id);
            this.comboBox_EForm.SelectedValue = this.cnt.GetCountryId(id);

            this.comboBox_Spec.SelectedValue = this.cnt.GetSpecId(id);

            this.comboBox_Adv.SelectedValue = this.cnt.GetAdvisorId(id);

            this.dateTimePicker_Asgn.Value = this.cnt.GetAsignDate(id);

            var dtDef = this.cnt.GetDefDate(id);
            var score = this.cnt.GetScore(id);

            if (score != null && dtDef != null)
            {
                this.checkBox1.Checked = true;
                this.checkBox1.Enabled = false;
                this.textBox_Score.Text = score.ToString();
                this.dateTimePicker_Def.Value = (DateTime)dtDef;

            }

        }

        public AddEditThesisForm(Model cnt)
        {
            this.cnt = cnt;
            InitializeComponent();
            modifyLook();
            initFinished = true;

            this.Text = "Add Thesis";

        }

        public void modifyLook()
        {

            dateTimePicker_Def.Format = DateTimePickerFormat.Custom;
            dateTimePicker_Def.CustomFormat = " ";
            dateTimePicker_Asgn.Format = DateTimePickerFormat.Custom;
            dateTimePicker_Asgn.CustomFormat = "dd.M.yyyy";


            #region ComboBoxes Init

            var query = this.cnt.GetCountries();
            this.comboBox_Country.DataSource = query;
            this.comboBox_Country.DisplayMember = "NAME";
            this.comboBox_Country.ValueMember = "ID";
            this.comboBox_Country.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Country.SelectedValue = 0;

            query = this.cnt.GetDegrees();
            this.comboBox_Deg.DataSource = query;
            this.comboBox_Deg.DisplayMember = "NAME";
            this.comboBox_Deg.ValueMember = "ID";
            this.comboBox_Deg.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Deg.SelectedValue = 0;

            query = this.cnt.GetFaculties();
            this.comboBox_Fac.DataSource = query;
            this.comboBox_Fac.DisplayMember = "NAME";
            this.comboBox_Fac.ValueMember = "ID";
            this.comboBox_Fac.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Fac.SelectedValue = 0;


            query = this.cnt.GetSpecialities();
            this.comboBox_Spec.DataSource = query;
            this.comboBox_Spec.DisplayMember = "NAME";
            this.comboBox_Spec.ValueMember = "ID";
            this.comboBox_Spec.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Spec.SelectedValue = 0;

            query = this.cnt.GetFaculties();
            this.comboBox_Fac.DataSource = query;
            this.comboBox_Fac.DisplayMember = "NAME";
            this.comboBox_Fac.ValueMember = "ID";
            this.comboBox_Fac.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Fac.SelectedValue = 0;

            query = this.cnt.GetOfficials();
            this.comboBox_Adv.DataSource = query;
            this.comboBox_Adv.DisplayMember = "NAME";
            this.comboBox_Adv.ValueMember = "ID";
            this.comboBox_Adv.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Adv.SelectedValue = 0;

            query = this.cnt.GetDepartaments();
            this.comboBox_Dep.DataSource = query;
            this.comboBox_Dep.DisplayMember = "NAME";
            this.comboBox_Dep.ValueMember = "ID";
            this.comboBox_Dep.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_Dep.SelectedValue = 0;

            query = this.cnt.GetEduForms();
            this.comboBox_EForm.DataSource = query;
            this.comboBox_EForm.DisplayMember = "NAME";
            this.comboBox_EForm.ValueMember = "ID";
            this.comboBox_EForm.DropDownStyle = ComboBoxStyle.DropDownList;
            this.comboBox_EForm.SelectedValue = 0;

            #endregion

        }

        private void button_Save_Click(object sender, EventArgs e)
        {

            DateTime? dtDef = null;
            double? score = null;

            if (this.checkBox1.Checked)
            {
                dtDef = this.dateTimePicker_Def.Value;
                score = float.Parse(this.textBox_Score.Text);
            }

            if (this.id == 0)
            {
                this.id = cnt.InsertThesis(this.textBox_FName.Text,
                           this.textBox_SName.Text,
                           this.textBox_LName.Text,
                           this.textBox_FacN.Text,
                           (int)this.comboBox_Country.SelectedValue,
                           (int)this.comboBox_Spec.SelectedValue,
                           (int)this.comboBox_EForm.SelectedValue,
                           this.textBox_Email.Text,
                           this.textBox_Title.Text,
                           (int)this.comboBox_Adv.SelectedValue,
                           this.richTextBox_Annot.Text,
                           this.dateTimePicker_Asgn.Value,
                           dtDef,
                           score
                          );

            }
            else
            {
                //update
                cnt.UpdateThesis(this.id,
                           this.textBox_FName.Text,
                           this.textBox_SName.Text,
                           this.textBox_LName.Text,
                           this.textBox_FacN.Text,
                           (int)this.comboBox_Country.SelectedValue,
                           (int)this.comboBox_Spec.SelectedValue,
                           (int)this.comboBox_EForm.SelectedValue,
                           this.textBox_Email.Text,
                           this.textBox_Title.Text,
                           (int)this.comboBox_Adv.SelectedValue,
                           this.richTextBox_Annot.Text,
                           this.dateTimePicker_Asgn.Value,
                           dtDef,
                           score
                          );

            }


            MessageBox.Show("Промените са запазени!", "Операцията е завършена", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            if (this.checkBox1.Checked)
            {
                this.dateTimePicker_Def.Enabled = true;
                dateTimePicker_Def.Format = DateTimePickerFormat.Custom;
                dateTimePicker_Def.CustomFormat = "dd.M.yyyy";
                this.textBox_Score.Enabled = true;
            }
            else
            {
                this.dateTimePicker_Def.Enabled = false;
                dateTimePicker_Def.Format = DateTimePickerFormat.Custom;
                dateTimePicker_Def.CustomFormat = " ";
                this.textBox_Score.Enabled = false;
            }
        }

        private void button_AddFiles_Click(object sender, EventArgs e)
        {

            if (this.id == 0)
            {
                MessageBox.Show("You have to save the thesis first", "Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            FilesForm ffw = new FilesForm(this.cnt, this.id);
            ffw.ShowDialog();

        }

        private void button_AddKwords_Click(object sender, EventArgs e)
        {
            if (this.id == 0)
            {
                MessageBox.Show("You have to save the thesis first", "Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            KeywordsForm kwf = new KeywordsForm(this.cnt, this.id);
            kwf.ShowDialog();

        }

        private void button_Close_Click(object sender, EventArgs e)
        {
            this.Dispose();
        }

        private void comboBox_Deg_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (!this.initFinished || this.comboBox_Deg.Focused == false)
                return;

            //clean dependable
            this.comboBox_Spec.SelectedValue = 0;

            //refilter
            var query = this.cnt.GetSpecialities((int)this.comboBox_Deg.SelectedValue, (int)this.comboBox_Fac.SelectedValue);
            this.comboBox_Spec.DataSource = query;


        }


        private void comboBox_Fac_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (!this.initFinished || !this.comboBox_Fac.Focused)
                return;

            //clean dependable
            this.comboBox_Spec.SelectedValue = 0;
            this.comboBox_Dep.SelectedValue = 0;

            //refilter specialities
            var query = this.cnt.GetSpecialities((int)this.comboBox_Deg.SelectedValue, (int)this.comboBox_Fac.SelectedValue);
            this.comboBox_Spec.DataSource = query;

            //refilter departaments
            query = this.cnt.GetDepartaments((int)this.comboBox_Fac.SelectedValue);
            query.Insert(0, new DropDownType { ID = 0, NAME = "===" });
            this.comboBox_Dep.DataSource = query;

        }

        private void comboBox_Spec_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (!this.initFinished || (int)this.comboBox_Spec.SelectedValue == 0)
                return;

            this.comboBox_Fac.SelectedValue = this.cnt.GetFacultyId((int)this.comboBox_Spec.SelectedValue);
            this.comboBox_Deg.SelectedValue = this.cnt.GetDegreeId((int)this.comboBox_Spec.SelectedValue);

        }

        private void comboBox_Dep_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (!this.initFinished || !this.comboBox_Dep.Focused)
                return;

            //clean dependable
            this.comboBox_Adv.SelectedValue = 0;

            //refilter advisors
            var query = this.cnt.GetOfficials((int)this.comboBox_Dep.SelectedValue);
            this.comboBox_Adv.DataSource = query;
        }

        private void comboBox_Adv_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (!this.initFinished || (int)this.comboBox_Adv.SelectedValue == 0)
                return;

            this.comboBox_Dep.SelectedValue = this.cnt.GetDepartamentId((int)this.comboBox_Adv.SelectedValue);

        }

        //code over this marker
    }
}

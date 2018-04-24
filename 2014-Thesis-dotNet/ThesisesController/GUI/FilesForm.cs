using System.Windows.Forms;
using System;
using System.IO;

namespace ThesesController.GUI
{
    public partial class FilesForm : Form
    {

        Model cnt;
        int id;

        public FilesForm(Model cnt, int id)
        {
            this.cnt = cnt;
            this.id = id;

            InitializeComponent();

            this.Height = this.Height / 2;
            this.splitContainer1.Panel2Collapsed = true;
            this.label4.Visible = false;
            this.comboBox_Owner.Visible = false;

            int specId = this.cnt.GetSpecId(id);
            int facId = this.cnt.GetFacultyId(specId);

            var query = this.cnt.GetOfficialsByFac(facId);
            this.comboBox_Owner.DataSource = query;
            this.comboBox_Owner.DisplayMember = "NAME";
            this.comboBox_Owner.ValueMember = "ID";

            query = this.cnt.GetFileTypes();
            this.comboBox_Type.DataSource = query;
            this.comboBox_Type.DisplayMember = "NAME";
            this.comboBox_Type.ValueMember = "ID";

            this.dataGridView_Files.MultiSelect = false;
            this.dataGridView_Files.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            var queryFiles = this.cnt.GetFilesDG(this.id);
            this.dataGridView_Files.DataSource = queryFiles;
            this.dataGridView_Files.Columns["ID"].Visible = false;
            this.dataGridView_Files.Columns["CONTENT"].Visible = false;
            this.dataGridView_Files.Columns["ISPUBLIC"].Visible = false;
            this.dataGridView_Files.Columns["OWNER"].ReadOnly = true;
            this.dataGridView_Files.Columns["TYPE"].ReadOnly = true;
            this.dataGridView_Files.Columns["DESCRIPTION"].ReadOnly = true;
            this.dataGridView_Files.Columns["DT_ADDED"].ReadOnly = true;
            //
            this.dataGridView_Files.Columns["TYPE"].HeaderText = "Тип на документа";
            this.dataGridView_Files.Columns["OWNER"].HeaderText = "Собственик";
            this.dataGridView_Files.Columns["DESCRIPTION"].HeaderText = "Описание";
            this.dataGridView_Files.Columns["EXT"].HeaderText = "Разш.";
            this.dataGridView_Files.Columns["DT_ADDED"].HeaderText = "Дата създаване";

            //fill checkboxes
            this.Show();

            fillCheckBoxes();

            this.Hide();

            dataGridView_Files.CurrentCell = null;
            dataGridView_Files.Rows[0].Visible = false;


        }

        private void button_New_Click(object sender, System.EventArgs e)
        {
            this.splitContainer1.Panel2Collapsed = false;
            this.splitContainer1.Panel1Collapsed = true;
            this.button_New.Visible = false;
        }

        private void button_Cancel_Click(object sender, System.EventArgs e)
        {

            this.splitContainer1.Panel2Collapsed = true;
            this.splitContainer1.Panel1Collapsed = false;
            this.button_New.Visible = true;

        }

        private void button_Save_Click(object sender, System.EventArgs e)
        {
            //confirm
            //if (MessageBox.Show("Once inserted the record can not be modified. Are you sure you want to proceeed?"
            //    , "Confirm insert", MessageBoxButtons.OKCancel) != DialogResult.OK)
            //{
            //    return;
            //}
            if (MessageBox.Show("Веднъж въведен запис не може да бъде променен или изтрит. Искате ли да продължите?"
               , "Потвърди въвеждане", MessageBoxButtons.OKCancel) != DialogResult.OK)
            {
                return;
            }
            //insert
            String location = this.textBox_fileLocation.Text.Replace("/","//");
            String fileExtension = Path.GetExtension(location);
            FileStream st = new FileStream(@location, FileMode.Open);
            var size = (int)new FileInfo(location).Length;

            byte[] buffer = new byte[st.Length];
            st.Read(buffer, 0, (int)st.Length);
            st.Close();


            byte isPublic;
            if (this.checkBox_PubAcc.Checked)
                isPublic = 1;
            else
                isPublic = 0;


            this.cnt.InsertFile(this.id,
                (int)this.comboBox_Type.SelectedValue,
                (int)this.comboBox_Owner.SelectedValue,
                fileExtension,
                buffer,
                this.richTextBox_Desc.Text,
                isPublic,
                size);


            //reload
            var queryFiles = this.cnt.GetFilesDG(this.id);
            this.dataGridView_Files.DataSource = queryFiles;
            fillCheckBoxes();
            dataGridView_Files.CurrentCell = null;
            dataGridView_Files.Rows[0].Visible = false;
            this.textBox_fileLocation.Text = "";
            this.comboBox_Type.SelectedValue = 0;
            this.comboBox_Type.SelectedValue = 0;
            this.richTextBox_Desc.Text = "";

            //switch view
            this.splitContainer1.Panel2Collapsed = true;
            this.splitContainer1.Panel1Collapsed = false;
            this.button_New.Visible = true;

        }

        private void button_Browse_Click(object sender, System.EventArgs e)
        {

            OpenFileDialog browseFile = new OpenFileDialog();

            //  browseFile.Filter = "XML Files (*.xml)|*.xml";
            browseFile.Title = "Избери файл";

            if (browseFile.ShowDialog() == DialogResult.Cancel)

                return;

            try
            {

                this.textBox_fileLocation.Text = browseFile.FileName;

            }

            catch (Exception)
            {

                MessageBox.Show("Грешка при прочитане на файла", "Грешка",

                MessageBoxButtons.OK, MessageBoxIcon.Exclamation);

            }
        }

        private void comboBox_Type_SelectedIndexChanged(object sender, EventArgs e)
        {

            if (this.comboBox_Type.Text.Trim() == "Рецензия")
            {
                this.comboBox_Owner.Visible = true;
                this.label4.Visible = true;
            }
            else
            {
                this.comboBox_Owner.Visible = false;
                this.label4.Visible = false;
            }

        }

        private void dataGridView_Files_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {


            int col = this.dataGridView_Files.CurrentCell.ColumnIndex;
            int row = this.dataGridView_Files.CurrentCell.RowIndex;
            int id = (int)this.dataGridView_Files.Rows[row].Cells["ID"].Value;


            if (col == 1)
            {
                byte[] bin = (byte[])this.dataGridView_Files.Rows[row].Cells["CONTENT"].Value;
                var name = ((string)this.dataGridView_Files.Rows[row].Cells["TYPE"].Value).Trim() + " " +
                  String.Format("{0:yyyyMMdd}", ((DateTime)this.dataGridView_Files.Rows[row].Cells["DT_ADDED"].Value)) +
                    (string)this.dataGridView_Files.Rows[row].Cells["EXT"].Value;

                this.saveFile(bin, name);
                return;
            }

            if (col != 0)
                return;


            byte val = (byte)((byte)this.dataGridView_Files.Rows[row].Cells["ISPUBLIC"].Value ^ (byte)1);
            this.dataGridView_Files.Rows[row].Cells["ISPUBLIC"].Value = val;

            this.cnt.UpdateDocVisibility(id, val);
            fillCheckBoxes();

        }


        public void saveFile(byte[] content, string name)
        {

            SaveFileDialog saveFileDialog1 = new SaveFileDialog();
            //saveFileDialog1.Filter = "JPeg Image|*.jpg|Bitmap Image|*.bmp|Gif Image|*.gif";
            saveFileDialog1.Title = "Запази файл";
            saveFileDialog1.FileName = name;
            saveFileDialog1.ShowDialog();

            // If the file name is not an empty string open it for saving. 
            if (saveFileDialog1.FileName == "")
                return;


            File.WriteAllBytes(saveFileDialog1.FileName, content);


        }


        void fillCheckBoxes()
        {

            foreach (DataGridViewRow row in this.dataGridView_Files.Rows)
            {
                var cel = row.Cells[0] as DataGridViewCheckBoxCell;
                cel.Value = Convert.ToBoolean(row.Cells["ISPUBLIC"].Value);
            }

        }

        private void translteToEnglish()
        {

            this.label1.Text = "File:";
            this.label2.Text = "Type:";
            this.label3.Text = "Description:";
            this.label4.Text = "Owner:";

            this.button_Browse.Text = "Browse";
            this.button_Cancel.Text = "Cancel";
            this.button_Save.Text = "Save";
            this.button_New.Text = "New";
        }


    }
}

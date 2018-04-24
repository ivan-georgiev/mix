using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace ThesesController.GUI
{
    public partial class KeywordsForm : Form
    {

        Model cnt;
        int id;

        public KeywordsForm(Model cnt, int id)
        {
            InitializeComponent();

            this.cnt = cnt;
            this.id = id;

            this.comboBox_Keyword.AutoCompleteMode = AutoCompleteMode.SuggestAppend;
            this.comboBox_Keyword.AutoCompleteSource = AutoCompleteSource.CustomSource;

            this.reloadForm();

            this.comboBox_Keyword.DisplayMember = "NAME";
            this.comboBox_Keyword.ValueMember = "NAME";

            this.listBox_Kwords.DisplayMember = "NAME";
            this.listBox_Kwords.ValueMember = "ID";

            ToolTip toolTip = new ToolTip();
            toolTip.SetToolTip(listBox_Kwords, "Кликни два пъти за премахване на дума");

        }



        private void reloadForm()
        {

            List<DropDownType> query = this.cnt.GetKeywords();
            this.comboBox_Keyword.DataSource = query;


            List<string> list = new List<string>();
            foreach (DropDownType cur in query)
            {
                list.Add(cur.NAME);
            }

            this.comboBox_Keyword.AutoCompleteCustomSource.AddRange(list.ToArray());

            query = this.cnt.GetKeywords(this.id);
            this.listBox_Kwords.DataSource = query;

        }


        private void button_Add_Click(object sender, EventArgs e)
        {
            String word = this.comboBox_Keyword.Text;

            if (!this.cnt.StringIsNotEmpty(word))
                return;

            int index = word.IndexOf(' ');

            if (index != -1)
            {
                word = word.Substring(0, index);
            }

            this.cnt.InsertKeyword(word, this.id);
            reloadForm();
        }

        private void listBox_Kwords_DoubleClick(object sender, EventArgs e)
        {

            var selId = (int)this.listBox_Kwords.SelectedValue;

            this.cnt.DeleteKeywordAssoc(selId);

            reloadForm();

        }

    }
}

namespace ThesesController.GUI
{
    partial class FilesForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.textBox_fileLocation = new System.Windows.Forms.TextBox();
            this.button_Browse = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.comboBox_Type = new System.Windows.Forms.ComboBox();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.dataGridView_Files = new System.Windows.Forms.DataGridView();
            this.richTextBox_Desc = new System.Windows.Forms.RichTextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.button_Cancel = new System.Windows.Forms.Button();
            this.button_Save = new System.Windows.Forms.Button();
            this.comboBox_Owner = new System.Windows.Forms.ComboBox();
            this.checkBox_PubAcc = new System.Windows.Forms.CheckBox();
            this.button_New = new System.Windows.Forms.Button();
            this.Public = new System.Windows.Forms.DataGridViewCheckBoxColumn();
            this.Get = new System.Windows.Forms.DataGridViewButtonColumn();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView_Files)).BeginInit();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 10);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(39, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Файл:";
            // 
            // textBox_fileLocation
            // 
            this.textBox_fileLocation.Location = new System.Drawing.Point(50, 7);
            this.textBox_fileLocation.Name = "textBox_fileLocation";
            this.textBox_fileLocation.ReadOnly = true;
            this.textBox_fileLocation.Size = new System.Drawing.Size(181, 20);
            this.textBox_fileLocation.TabIndex = 1;
            // 
            // button_Browse
            // 
            this.button_Browse.Location = new System.Drawing.Point(244, 5);
            this.button_Browse.Name = "button_Browse";
            this.button_Browse.Size = new System.Drawing.Size(75, 23);
            this.button_Browse.TabIndex = 2;
            this.button_Browse.Text = "Избери";
            this.button_Browse.UseVisualStyleBackColor = true;
            this.button_Browse.Click += new System.EventHandler(this.button_Browse_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(0, 41);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(29, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "Вид:";
            // 
            // comboBox_Type
            // 
            this.comboBox_Type.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox_Type.FormattingEnabled = true;
            this.comboBox_Type.Location = new System.Drawing.Point(50, 38);
            this.comboBox_Type.Name = "comboBox_Type";
            this.comboBox_Type.Size = new System.Drawing.Size(121, 21);
            this.comboBox_Type.TabIndex = 4;
            this.comboBox_Type.SelectedIndexChanged += new System.EventHandler(this.comboBox_Type_SelectedIndexChanged);
            // 
            // splitContainer1
            // 
            this.splitContainer1.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.splitContainer1.Location = new System.Drawing.Point(3, 36);
            this.splitContainer1.Name = "splitContainer1";
            this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.dataGridView_Files);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.richTextBox_Desc);
            this.splitContainer1.Panel2.Controls.Add(this.label3);
            this.splitContainer1.Panel2.Controls.Add(this.label4);
            this.splitContainer1.Panel2.Controls.Add(this.button_Cancel);
            this.splitContainer1.Panel2.Controls.Add(this.button_Save);
            this.splitContainer1.Panel2.Controls.Add(this.comboBox_Owner);
            this.splitContainer1.Panel2.Controls.Add(this.checkBox_PubAcc);
            this.splitContainer1.Panel2.Controls.Add(this.label1);
            this.splitContainer1.Panel2.Controls.Add(this.textBox_fileLocation);
            this.splitContainer1.Panel2.Controls.Add(this.label2);
            this.splitContainer1.Panel2.Controls.Add(this.button_Browse);
            this.splitContainer1.Panel2.Controls.Add(this.comboBox_Type);
            this.splitContainer1.Size = new System.Drawing.Size(330, 528);
            this.splitContainer1.SplitterDistance = 264;
            this.splitContainer1.TabIndex = 7;
            // 
            // dataGridView_Files
            // 
            this.dataGridView_Files.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.dataGridView_Files.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView_Files.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.Public,
            this.Get});
            this.dataGridView_Files.Location = new System.Drawing.Point(3, 3);
            this.dataGridView_Files.Name = "dataGridView_Files";
            this.dataGridView_Files.RowHeadersVisible = false;
            this.dataGridView_Files.Size = new System.Drawing.Size(324, 258);
            this.dataGridView_Files.TabIndex = 0;
            this.dataGridView_Files.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.dataGridView_Files_CellContentClick);
            // 
            // richTextBox_Desc
            // 
            this.richTextBox_Desc.Location = new System.Drawing.Point(6, 119);
            this.richTextBox_Desc.Name = "richTextBox_Desc";
            this.richTextBox_Desc.Size = new System.Drawing.Size(313, 51);
            this.richTextBox_Desc.TabIndex = 15;
            this.richTextBox_Desc.Text = "";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(3, 103);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(60, 13);
            this.label3.TabIndex = 14;
            this.label3.Text = "Описание:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(3, 71);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(40, 13);
            this.label4.TabIndex = 13;
            this.label4.Text = "Автор:";
            // 
            // button_Cancel
            // 
            this.button_Cancel.Location = new System.Drawing.Point(244, 180);
            this.button_Cancel.Name = "button_Cancel";
            this.button_Cancel.Size = new System.Drawing.Size(75, 23);
            this.button_Cancel.TabIndex = 12;
            this.button_Cancel.Text = "Отмени";
            this.button_Cancel.UseVisualStyleBackColor = true;
            this.button_Cancel.Click += new System.EventHandler(this.button_Cancel_Click);
            // 
            // button_Save
            // 
            this.button_Save.Location = new System.Drawing.Point(156, 180);
            this.button_Save.Name = "button_Save";
            this.button_Save.Size = new System.Drawing.Size(75, 23);
            this.button_Save.TabIndex = 11;
            this.button_Save.Text = "Запази";
            this.button_Save.UseVisualStyleBackColor = true;
            this.button_Save.Click += new System.EventHandler(this.button_Save_Click);
            // 
            // comboBox_Owner
            // 
            this.comboBox_Owner.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox_Owner.FormattingEnabled = true;
            this.comboBox_Owner.Location = new System.Drawing.Point(50, 68);
            this.comboBox_Owner.Name = "comboBox_Owner";
            this.comboBox_Owner.Size = new System.Drawing.Size(121, 21);
            this.comboBox_Owner.TabIndex = 10;
            // 
            // checkBox_PubAcc
            // 
            this.checkBox_PubAcc.AutoSize = true;
            this.checkBox_PubAcc.Location = new System.Drawing.Point(177, 42);
            this.checkBox_PubAcc.Name = "checkBox_PubAcc";
            this.checkBox_PubAcc.Size = new System.Drawing.Size(108, 17);
            this.checkBox_PubAcc.TabIndex = 7;
            this.checkBox_PubAcc.Text = "Публичен досъп";
            this.checkBox_PubAcc.UseVisualStyleBackColor = true;
            // 
            // button_New
            // 
            this.button_New.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.button_New.Location = new System.Drawing.Point(255, 7);
            this.button_New.Name = "button_New";
            this.button_New.Size = new System.Drawing.Size(75, 23);
            this.button_New.TabIndex = 8;
            this.button_New.Text = "Нов";
            this.button_New.UseVisualStyleBackColor = true;
            this.button_New.Click += new System.EventHandler(this.button_New_Click);
            // 
            // Public
            // 
            this.Public.HeaderText = "Публ.";
            this.Public.Name = "Public";
            this.Public.Resizable = System.Windows.Forms.DataGridViewTriState.False;
            this.Public.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.Automatic;
            this.Public.Width = 50;
            // 
            // Get
            // 
            this.Get.HeaderText = "Виж";
            this.Get.Name = "Get";
            this.Get.Resizable = System.Windows.Forms.DataGridViewTriState.False;
            this.Get.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.Automatic;
            this.Get.Text = "Виж";
            this.Get.UseColumnTextForButtonValue = true;
            this.Get.Width = 33;
            // 
            // FilesForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(338, 566);
            this.Controls.Add(this.button_New);
            this.Controls.Add(this.splitContainer1);
            this.Name = "FilesForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Управление на файлове";
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.Panel2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView_Files)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textBox_fileLocation;
        private System.Windows.Forms.Button button_Browse;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.ComboBox comboBox_Type;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.DataGridView dataGridView_Files;
        private System.Windows.Forms.RichTextBox richTextBox_Desc;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button button_Cancel;
        private System.Windows.Forms.Button button_Save;
        private System.Windows.Forms.ComboBox comboBox_Owner;
        private System.Windows.Forms.CheckBox checkBox_PubAcc;
        private System.Windows.Forms.Button button_New;
        private System.Windows.Forms.DataGridViewCheckBoxColumn Public;
        private System.Windows.Forms.DataGridViewButtonColumn Get;
    }
}
namespace ThesesController.GUI
{
    partial class KeywordsForm
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
            this.comboBox_Keyword = new System.Windows.Forms.ComboBox();
            this.button_Add = new System.Windows.Forms.Button();
            this.listBox_Kwords = new System.Windows.Forms.ListBox();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(1, 13);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(38, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Дума:";
            // 
            // comboBox_Keyword
            // 
            this.comboBox_Keyword.FormattingEnabled = true;
            this.comboBox_Keyword.Location = new System.Drawing.Point(54, 10);
            this.comboBox_Keyword.Name = "comboBox_Keyword";
            this.comboBox_Keyword.Size = new System.Drawing.Size(140, 21);
            this.comboBox_Keyword.TabIndex = 1;
            // 
            // button_Add
            // 
            this.button_Add.Location = new System.Drawing.Point(200, 8);
            this.button_Add.Name = "button_Add";
            this.button_Add.Size = new System.Drawing.Size(59, 23);
            this.button_Add.TabIndex = 2;
            this.button_Add.Text = "Добави";
            this.button_Add.UseVisualStyleBackColor = true;
            this.button_Add.Click += new System.EventHandler(this.button_Add_Click);
            // 
            // listBox_Kwords
            // 
            this.listBox_Kwords.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(204)));
            this.listBox_Kwords.FormattingEnabled = true;
            this.listBox_Kwords.ItemHeight = 16;
            this.listBox_Kwords.Location = new System.Drawing.Point(4, 40);
            this.listBox_Kwords.Name = "listBox_Kwords";
            this.listBox_Kwords.Size = new System.Drawing.Size(255, 132);
            this.listBox_Kwords.TabIndex = 3;
            this.listBox_Kwords.DoubleClick += new System.EventHandler(this.listBox_Kwords_DoubleClick);
            // 
            // KeywordsForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(263, 179);
            this.Controls.Add(this.listBox_Kwords);
            this.Controls.Add(this.button_Add);
            this.Controls.Add(this.comboBox_Keyword);
            this.Controls.Add(this.label1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "KeywordsForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Ключови думи";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ComboBox comboBox_Keyword;
        private System.Windows.Forms.Button button_Add;
        private System.Windows.Forms.ListBox listBox_Kwords;
    }
}
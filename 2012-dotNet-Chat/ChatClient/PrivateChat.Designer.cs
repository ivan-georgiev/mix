using System;
namespace ClientApp
{
    public partial class PrivateChat
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

            cc.chatWindows.Remove(this.Text);

            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);


        }


        delegate void AddTextRowCallback(string text);

        public void AddTextRow(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.richTextBoxPrChat.InvokeRequired)
            {
                AddTextRowCallback d = new AddTextRowCallback(AddTextRow);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.richTextBoxPrChat.AppendText("[" + DateTime.Now.ToString("HH:mm") + "] " + text + "\n");
                        
            }
        }

        public void SetFocusOnChat()
        {
            this.textBoxPrCType.Focus();
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.richTextBoxPrChat = new System.Windows.Forms.RichTextBox();
            this.textBoxPrCType = new System.Windows.Forms.TextBox();
            this.buttonPrCSend = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // richTextBoxPrChat
            // 
            this.richTextBoxPrChat.AcceptsTab = true;
            this.richTextBoxPrChat.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.richTextBoxPrChat.Location = new System.Drawing.Point(2, 2);
            this.richTextBoxPrChat.Name = "richTextBoxPrChat";
            this.richTextBoxPrChat.ReadOnly = true;
            this.richTextBoxPrChat.Size = new System.Drawing.Size(496, 254);
            this.richTextBoxPrChat.TabIndex = 0;
            this.richTextBoxPrChat.Text = "";
            // 
            // textBoxPrCType
            // 
            this.textBoxPrCType.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxPrCType.Location = new System.Drawing.Point(2, 262);
            this.textBoxPrCType.Name = "textBoxPrCType";
            this.textBoxPrCType.Size = new System.Drawing.Size(415, 20);
            this.textBoxPrCType.TabIndex = 1;
            this.textBoxPrCType.TextChanged += new System.EventHandler(this.textBoxPrCType_TextChanged);
            this.textBoxPrCType.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.textBoxPrCType_KeyPress);
            // 
            // buttonPrCSend
            // 
            this.buttonPrCSend.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.buttonPrCSend.Enabled = false;
            this.buttonPrCSend.Location = new System.Drawing.Point(423, 262);
            this.buttonPrCSend.Name = "buttonPrCSend";
            this.buttonPrCSend.Size = new System.Drawing.Size(75, 23);
            this.buttonPrCSend.TabIndex = 2;
            this.buttonPrCSend.Text = "Send";
            this.buttonPrCSend.UseVisualStyleBackColor = true;
            this.buttonPrCSend.Click += new System.EventHandler(this.buttonPrCSend_Click);
            // 
            // PrivateChat
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(501, 285);
            this.Controls.Add(this.buttonPrCSend);
            this.Controls.Add(this.textBoxPrCType);
            this.Controls.Add(this.richTextBoxPrChat);
            this.Name = "PrivateChat";
            this.Text = "PrivateChat";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.RichTextBox richTextBoxPrChat;
        private System.Windows.Forms.TextBox textBoxPrCType;
        private System.Windows.Forms.Button buttonPrCSend;
    }
}
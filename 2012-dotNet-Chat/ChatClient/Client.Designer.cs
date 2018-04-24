using System.Windows.Forms;
using System;
namespace ClientApp
{
    public partial class Client
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

        protected override void OnFormClosing(FormClosingEventArgs e)
        {
            base.OnFormClosing(e);

            if (cc == null || !cc.IsConnected())
                return;

            if (e.CloseReason == CloseReason.WindowsShutDown) return;

            // Confirm user wants to close
            switch (MessageBox.Show(this, "Are you sure you want to close?", "Closing", MessageBoxButtons.YesNo))
            {
                case DialogResult.No:
                    e.Cancel = true;
                    break;
                default:
                    break;
            }

            cc.Disconnect();

        }

        delegate void AddTextRowCallback(string text);

        public void AddTextRow(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.richTextBoxMainAct.InvokeRequired)
            {
                AddTextRowCallback d = new AddTextRowCallback(AddTextRow);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.richTextBoxMainAct.AppendText("[" + DateTime.Now.ToString("HH:mm") + "] " + text + "\n");
            }
        }

        public void addUser(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.richTextBoxMainAct.InvokeRequired)
            {
                AddTextRowCallback d = new AddTextRowCallback(addUser);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.listBoxMainUsers.Items.Add(text);
            }
        }

        public void removeUser(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.richTextBoxMainAct.InvokeRequired)
            {
                AddTextRowCallback d = new AddTextRowCallback(removeUser);
                this.Invoke(d, new object[] { text });
            }
            else if (this.listBoxMainUsers.Items.Contains(text))
            {
                this.listBoxMainUsers.Items.Remove(text);
            }
        }


        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.richTextBoxMainAct = new System.Windows.Forms.RichTextBox();
            this.textBoxMainType = new System.Windows.Forms.TextBox();
            this.buttonMainSend = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.textBoxMainUserName = new System.Windows.Forms.TextBox();
            this.textBoxMainPort = new System.Windows.Forms.TextBox();
            this.buttonMainConnectDisconnect = new System.Windows.Forms.Button();
            this.textBoxMainAddress = new System.Windows.Forms.TextBox();
            this.listBoxMainUsers = new System.Windows.Forms.ListBox();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.SuspendLayout();
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.richTextBoxMainAct);
            this.splitContainer1.Panel1.Controls.Add(this.textBoxMainType);
            this.splitContainer1.Panel1.Controls.Add(this.buttonMainSend);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.label1);
            this.splitContainer1.Panel2.Controls.Add(this.textBoxMainUserName);
            this.splitContainer1.Panel2.Controls.Add(this.textBoxMainPort);
            this.splitContainer1.Panel2.Controls.Add(this.buttonMainConnectDisconnect);
            this.splitContainer1.Panel2.Controls.Add(this.textBoxMainAddress);
            this.splitContainer1.Panel2.Controls.Add(this.listBoxMainUsers);
            this.splitContainer1.Size = new System.Drawing.Size(688, 383);
            this.splitContainer1.SplitterDistance = 504;
            this.splitContainer1.TabIndex = 0;
            // 
            // richTextBoxMainAct
            // 
            this.richTextBoxMainAct.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.richTextBoxMainAct.Location = new System.Drawing.Point(3, 3);
            this.richTextBoxMainAct.Name = "richTextBoxMainAct";
            this.richTextBoxMainAct.ReadOnly = true;
            this.richTextBoxMainAct.Size = new System.Drawing.Size(498, 350);
            this.richTextBoxMainAct.TabIndex = 2;
            this.richTextBoxMainAct.Text = "";
            // 
            // textBoxMainType
            // 
            this.textBoxMainType.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxMainType.Enabled = false;
            this.textBoxMainType.Location = new System.Drawing.Point(0, 357);
            this.textBoxMainType.Name = "textBoxMainType";
            this.textBoxMainType.Size = new System.Drawing.Size(418, 20);
            this.textBoxMainType.TabIndex = 1;
            this.textBoxMainType.TextChanged += new System.EventHandler(this.textBox1_TextChanged);
            this.textBoxMainType.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.textBoxMainType_KeyPress);
            // 
            // buttonMainSend
            // 
            this.buttonMainSend.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.buttonMainSend.Enabled = false;
            this.buttonMainSend.Location = new System.Drawing.Point(427, 358);
            this.buttonMainSend.Name = "buttonMainSend";
            this.buttonMainSend.Size = new System.Drawing.Size(74, 23);
            this.buttonMainSend.TabIndex = 0;
            this.buttonMainSend.Text = "Send";
            this.buttonMainSend.UseVisualStyleBackColor = true;
            this.buttonMainSend.Click += new System.EventHandler(this.buttonMainSend_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 30);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(58, 13);
            this.label1.TabIndex = 5;
            this.label1.Text = "Username:";
            // 
            // textBoxMainUserName
            // 
            this.textBoxMainUserName.Location = new System.Drawing.Point(67, 27);
            this.textBoxMainUserName.Name = "textBoxMainUserName";
            this.textBoxMainUserName.Size = new System.Drawing.Size(110, 20);
            this.textBoxMainUserName.TabIndex = 4;
            this.textBoxMainUserName.TextChanged += new System.EventHandler(this.textBoxMainUserName_TextChanged);
            this.textBoxMainUserName.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.textBoxMainUserName_KeyPress);
            // 
            // textBoxMainPort
            // 
            this.textBoxMainPort.Location = new System.Drawing.Point(119, 3);
            this.textBoxMainPort.Name = "textBoxMainPort";
            this.textBoxMainPort.Size = new System.Drawing.Size(58, 20);
            this.textBoxMainPort.TabIndex = 3;
            this.textBoxMainPort.Text = "5555";
            // 
            // buttonMainConnectDisconnect
            // 
            this.buttonMainConnectDisconnect.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.buttonMainConnectDisconnect.Enabled = false;
            this.buttonMainConnectDisconnect.Location = new System.Drawing.Point(-1, 49);
            this.buttonMainConnectDisconnect.Name = "buttonMainConnectDisconnect";
            this.buttonMainConnectDisconnect.Size = new System.Drawing.Size(178, 23);
            this.buttonMainConnectDisconnect.TabIndex = 2;
            this.buttonMainConnectDisconnect.Text = "Connect";
            this.buttonMainConnectDisconnect.UseVisualStyleBackColor = true;
            this.buttonMainConnectDisconnect.Click += new System.EventHandler(this.buttonMainConnectDisconnect_Click_1);
            // 
            // textBoxMainAddress
            // 
            this.textBoxMainAddress.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxMainAddress.Location = new System.Drawing.Point(-1, 3);
            this.textBoxMainAddress.Name = "textBoxMainAddress";
            this.textBoxMainAddress.Size = new System.Drawing.Size(114, 20);
            this.textBoxMainAddress.TabIndex = 1;
            this.textBoxMainAddress.Text = "127.0.0.1";
            // 
            // listBoxMainUsers
            // 
            this.listBoxMainUsers.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.listBoxMainUsers.FormattingEnabled = true;
            this.listBoxMainUsers.Location = new System.Drawing.Point(-1, 78);
            this.listBoxMainUsers.Name = "listBoxMainUsers";
            this.listBoxMainUsers.Size = new System.Drawing.Size(178, 303);
            this.listBoxMainUsers.Sorted = true;
            this.listBoxMainUsers.TabIndex = 0;
            this.listBoxMainUsers.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.listBoxMainUsers_MouseDoubleClick);
            // 
            // Client
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(688, 383);
            this.Controls.Add(this.splitContainer1);
            this.Name = "Client";
            this.Text = "Client";
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel1.PerformLayout();
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.Panel2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.RichTextBox richTextBoxMainAct;
        private System.Windows.Forms.TextBox textBoxMainType;
        private System.Windows.Forms.Button buttonMainSend;
        private System.Windows.Forms.ListBox listBoxMainUsers;
        private System.Windows.Forms.Button buttonMainConnectDisconnect;
        private System.Windows.Forms.TextBox textBoxMainAddress;
        private System.Windows.Forms.TextBox textBoxMainPort;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textBoxMainUserName;

    }
}


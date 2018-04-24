using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Commons;

namespace ClientApp
{
    public partial class Client : Form
    {

        ClientController cc;

        public Client()
        {
            InitializeComponent();
            // RichTextBox.CheckForIllegalCrossThreadCalls=false;
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            if (this.textBoxMainType.Text.Trim().Length > 0)
                this.buttonMainSend.Enabled = true;
            else
                this.buttonMainSend.Enabled = false;
        }

        private void buttonMainSend_Click(object sender, EventArgs e)
        {

            this.cc.Send(new ChatMessage(this.textBoxMainUserName.Text, "", this.textBoxMainType.Text));
            this.textBoxMainType.Text = "";
        }

        private void textBoxMainPort_TextChanged(object sender, EventArgs e)
        {

        }

        private void buttonMainConnectDisconnect_Click(object sender, EventArgs e)
        {

        }

        private void buttonMainConnectDisconnect_Click_1(object sender, EventArgs e)
        {
            if (cc==null || !cc.IsConnected())
            cc = new ClientController(this);

            if (!cc.IsConnected())
            {
                cc.Connect(this.textBoxMainUserName.Text, this.textBoxMainAddress.Text, Int32.Parse(this.textBoxMainPort.Text));

                this.buttonMainConnectDisconnect.Text = "Disconnect";
                this.textBoxMainUserName.Enabled = false;
                this.textBoxMainAddress.Enabled = false;
                this.textBoxMainPort.Enabled = false;
                this.textBoxMainType.Enabled = true;
                this.buttonMainSend.Enabled = true;
            }
            else
            {
                cc.Disconnect();

                this.buttonMainConnectDisconnect.Text = "Connect";
                this.textBoxMainUserName.Enabled = true;
                this.textBoxMainAddress.Enabled = true;
                this.textBoxMainPort.Enabled = true;
                this.textBoxMainType.Enabled = false;
                this.buttonMainSend.Enabled = false;
                this.listBoxMainUsers.Items.Clear();
            }
        }

        private void textBoxMainType_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 13)
            {
                buttonMainSend_Click(sender, e);
            }

        }

        private void textBoxMainUserName_TextChanged(object sender, EventArgs e)
        {
            if (this.textBoxMainUserName.Text.Length > 0)
            {
                this.buttonMainConnectDisconnect.Enabled = true;
            }
            else
            {
                this.buttonMainConnectDisconnect.Enabled = false;
                
            }
        }

        private void listBoxMainUsers_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            //@TODO
            //get element selected and  create chat windows with it.
            String userName = (String) this.listBoxMainUsers.SelectedItem;

            if (cc.chatWindows.ContainsKey(userName))
            {
                cc.chatWindows[userName].Focus();
                cc.chatWindows[userName].SetFocusOnChat();
            }else
            {
                cc.createPrChatWindow(userName);
                cc.chatWindows[userName].SetFocusOnChat();
            }
        }

        private void textBoxMainUserName_KeyPress(object sender, KeyPressEventArgs e)
        {
            //connect
            if (e.KeyChar == 13 && this.textBoxMainUserName.Text.Length > 0)
            {
                buttonMainConnectDisconnect_Click_1(sender,e);
            }

        }

    }


}

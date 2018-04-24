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
    public partial class PrivateChat : Form
    {

        ClientController cc;

        public PrivateChat(ClientController cc)
        {
            InitializeComponent();
            this.cc = cc;
        }

        private void textBoxPrCType_TextChanged(object sender, EventArgs e)
        {
            if (textBoxPrCType.Text.Trim().Length > 0)
                this.buttonPrCSend.Enabled = true;
            else
                this.buttonPrCSend.Enabled = false;
        }

        private void buttonPrCSend_Click(object sender, EventArgs e)
        {
            this.AddTextRow("Me: " + this.textBoxPrCType.Text);
            cc.Send(new ChatMessage(this.cc.WhoAmI, this.Text, this.textBoxPrCType.Text));
            this.textBoxPrCType.Text = "";
        }

        private void textBoxPrCType_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == 13)
            {
                buttonPrCSend_Click(sender, e);
            }
        }

    }
}

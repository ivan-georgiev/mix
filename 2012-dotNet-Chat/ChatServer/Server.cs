using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace ServerApp
{
    public partial class Server : Form
    {

        ServerController sc;

        public Server()
        {
            InitializeComponent();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void buttonStart_Click(object sender, EventArgs e)
        {
            //start
            this.AddTextRow("Start\n");
            this.textBoxPort.Enabled = false;
            this.buttonStart.Enabled = false;
            this.buttonStop.Enabled = true;
            sc = new ServerController(this, Int32.Parse(this.textBoxPort.Text));
            sc.Start();

        }

        private void buttonStop_Click(object sender, EventArgs e)
        {
            //stop
            this.AddTextRow("Stop\n");
            this.textBoxPort.Enabled = true;
            this.buttonStart.Enabled = true;
            this.buttonStop.Enabled = false;
            sc.Stop();

        }


    }
}

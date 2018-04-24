using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using Commons;

namespace ClientApp
{
   public class ClientController
    {
        TcpClient tcpClient;
        Client clWindow;
        Thread listenThread;
        //personal chat windows
       public Dictionary<String, PrivateChat> chatWindows = new Dictionary<String, PrivateChat>(10);
       public String WhoAmI;

        public ClientController(Client clWindow)
        {
            this.clWindow = clWindow;
            tcpClient = new TcpClient();

        }

        public bool IsConnected()
        {
            return tcpClient.Connected;
        }


        // Connects the client to a server at the specified IP and port
        public void Connect(String userName, String address, int port)
        {

            IPEndPoint serverEndPoint =
               new IPEndPoint(IPAddress.Parse(address), port);

            tcpClient.Connect(serverEndPoint);

            //register username
            this.Send(new ChatUserControl(userName, true));
            WhoAmI = userName;

            listenThread = new Thread(new ThreadStart(delegate
           {
               Read();
           }));

            listenThread.Start();

            // Create a thread to read data sent from the server.
            //ThreadPool.QueueUserWorkItem(
            //   delegate 
            //   {
            //       Read();
            //   }
            //   );
        }

        // Sends object to the server.
        public void Send(Object userObject)
        {
            NetworkStream stream = tcpClient.GetStream();
            IFormatter formatter = new BinaryFormatter();
            formatter.Serialize(stream, userObject);
            stream.Flush();
            //stream.Close();
        }

        private void Read()
        {

            while (true)
            {
                NetworkStream strm = tcpClient.GetStream();

                IFormatter formatter = new BinaryFormatter();

                Object userObj = formatter.Deserialize(strm);

                strm.Flush();
                //strm.Close();

                if (userObj is ChatUserControl)
                {

                    String userName = ((ChatUserControl)userObj).UserName;
                    Boolean add = ((ChatUserControl)userObj).Active;
                    if (add)
                    {
                        this.clWindow.addUser(userName);


                    }
                    else
                    {
                        this.clWindow.removeUser(userName);
                    }
                }
                else if (userObj is ChatMessage)
                {

                    ChatMessage cm = ((ChatMessage)userObj);

                    String sender = cm.SenderName;
                    String receiver = cm.ReceiverName;
                    String message = cm.Message;


                    if (receiver.Equals(""))
                    {
                        //broadcast message
                        this.clWindow.AddTextRow(sender + ": " + message);
                    }
                    else
                    {
                        //personal message


                        PrivateChat pc;

                        if (!chatWindows.Keys.Contains(sender))
                        {
                            this.createPrChatWindow(sender);
                        }

                        pc = chatWindows[sender];

                        pc.AddTextRow(sender + ": " + message);
                    }


                }
                else
                {
                    //error;
                    break;
                }


            }

        }

        delegate void PrivateChatWindowCallback(string text);

        public void createPrChatWindow(string text)
        {
            if (this.clWindow.InvokeRequired)
            {
                PrivateChatWindowCallback pe = new PrivateChatWindowCallback(createPrChatWindow);
                this.clWindow.Invoke(pe, new object[] { text });
            }
            else
            {
                PrivateChat pr = new PrivateChat(this);
                pr.Text = text;
                chatWindows.Add(text, pr);
                pr.Show();
            }
        }


        public void Disconnect()
        {
            this.Send(new ChatUserControl(WhoAmI, false));
            listenThread.Abort();
            tcpClient.Close();

        }

    }

}

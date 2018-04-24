using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using Commons;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Threading;

namespace ServerApp
{
    class ServerController
    {
        private TcpListener tcpListener;
        private Thread listenThread;
        private Server mainW;

        private Dictionary<String, TcpClient> userList = new Dictionary<String, TcpClient>(50);


        public ServerController(Server mainW, int port)
        {
            this.tcpListener = new TcpListener(IPAddress.Any, port);
            this.mainW = mainW;
        }

        public void Start()
        {
            this.listenThread = new Thread(new ThreadStart(ListenForClients));
            this.listenThread.Start();
        }

        public void Stop()
        {
            try
            {
                foreach (KeyValuePair<string, TcpClient> item in this.userList)
                {
                    try
                    {
                        item.Value.Close();
                    }
                    catch
                    {
                        //do nothing on error
                    }
                }

                this.tcpListener.Stop();
                this.listenThread.Abort();
            }
            catch
            {
                //do nothing on error
            }
        }



        private void ListenForClients()
        {
            this.tcpListener.Start();

            this.mainW.AddTextRow("Waiting...\n");

            while (true)
            {
                //blocks until a client has connected to the server
                TcpClient client = this.tcpListener.AcceptTcpClient();

                //create a thread to handle communication with connected client
                Thread clientThread = new Thread(new ParameterizedThreadStart(ClientListener));
                clientThread.Start(client);

            }
        }

        private void ClientListener(object client)
        {
            TcpClient tcpClient = (TcpClient)client;
            NetworkStream clientStream = tcpClient.GetStream();
            IFormatter formatter = new BinaryFormatter();
            String userTreadName = "";

            // TEMP
            //ChatMessage cms = new ChatMessage("Gancho", "1", "test message 1");
            //this.sendToTcpClient(cms, tcpClient);
            //System.Threading.Thread.Sleep(5000);
            //cms = new ChatMessage("Gancho", "1", "test message 2");
            //this.sendToTcpClient(cms, tcpClient);
            //System.Threading.Thread.Sleep(5000);
            //cms = new ChatMessage("Gancho", "", "test message 3");
            //this.sendToTcpClient(cms, tcpClient);

            while (true)
            {

                try
                {

                    Object userObj = getFromClient(tcpClient);

                    if (userObj is ChatUserControl)
                    {

                        String userName = ((ChatUserControl)userObj).UserName;
                        Boolean add = ((ChatUserControl)userObj).Active;

                        if (add)
                        {
                            if (userName == "Admin" || this.userList.ContainsKey(userName))
                            {
                                ChatMessage cm = new ChatMessage("Admin", "", "Name already taken");
                                this.sendToTcpClient(cm, tcpClient);
                                break;
                            }

                            this.userList.Add(userName, tcpClient);
                            userTreadName = userName;
                            this.mainW.AddTextRow(userName + " logged");

                            this.sendToTcpClient(new ChatMessage("Admin", "", "Welcome, " + userName + "!"), tcpClient);

                            ChatUserControl cc;

                            foreach (KeyValuePair<string, TcpClient> item in this.userList)
                            {
                                cc = new ChatUserControl(item.Key, true);
                                this.sendToTcpClient(cc, tcpClient);

                                //send the user to other users list
                                if (!item.Key.Equals(userName))
                                {
                                    cc = new ChatUserControl(userName, true);
                                    this.sendToClient(cc, item.Key);
                                }
                            }
                        }
                        else
                        {
                            //logout event
                            ChatUserControl cc;

                            this.userList.Remove(userName);
                            this.mainW.AddTextRow(userName + " logged out");

                            foreach (KeyValuePair<string, TcpClient> item in this.userList)
                            {
                                cc = new ChatUserControl(userName, false);
                                this.sendToClient(cc, item.Key);
                            }

                            break;

                        }


                    }
                    else if (userObj is ChatMessage)
                    {

                        ChatMessage cm = ((ChatMessage)userObj);

                        String sender = cm.SenderName;
                        String receiver = cm.ReceiverName;
                        String message = cm.Message;


                        if (receiver.Equals("Admin"))
                        {
                            //@TODO
                            //Action message
                            //DO: Auth, Topic, Ban, Rename ...
                        }
                        else if (receiver.Equals(""))
                        {
                            //broadcast to all
                            this.mainW.AddTextRow(sender + " broadcast a message");


                            foreach (KeyValuePair<string, TcpClient> item in this.userList)
                            {

                                this.sendToTcpClient(cm, item.Value);

                            }

                        }
                        else
                        {
                            //private message - forward
                            this.mainW.AddTextRow("Private: "+sender+"->"+receiver);

                            if (this.userList.ContainsKey(receiver))
                            {
                                this.sendToClient(userObj, receiver);
                            }
                            else
                            {
                                cm = new ChatMessage(receiver, sender, "Admin: User is offline!");
                                this.sendToTcpClient(cm, tcpClient);

                            }

                        }

                    }
                    else
                    {
                        //error;
                        break;
                    }

                }
                catch
                {
                    //a socket error has occured

                    this.mainW.AddTextRow("Socket error for: " + userTreadName);
                    userList.Remove(userTreadName);
                    break;
                }


                // Console.WriteLine("To: " + tcpClient.Client.LocalEndPoint);
                // Console.WriteLine("From: " + tcpClient.Client.RemoteEndPoint);

            }

            //  tcpClient.Close();
        }


        public void sendToClient(Object userObject, String clientName)
        {
            try
            {
                TcpClient client;

                if (this.userList.ContainsKey(clientName))
                {
                    client = this.userList[clientName];

                    sendToTcpClient(userObject, client);

                }

            }
            catch
            {
                Console.WriteLine("Client Disconnected." + Environment.NewLine);

            }
        }

        private void sendToTcpClient(Object userObject, TcpClient client)
        {
            NetworkStream stream = client.GetStream();
            IFormatter formatter = new BinaryFormatter();
            formatter.Serialize(stream, userObject);
            stream.Flush();
        }

        public Object getFromClient(TcpClient client)
        {

            NetworkStream strm = client.GetStream();

            IFormatter formatter = new BinaryFormatter();

            Object userObject = formatter.Deserialize(strm);

            strm.Flush();

            return userObject;

        }

    }

}

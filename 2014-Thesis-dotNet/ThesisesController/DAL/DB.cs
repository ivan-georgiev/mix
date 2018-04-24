using System;
using System.Data;
using System.Data.SqlClient;
using System.Text.RegularExpressions;
using System.Reflection;

namespace ThesesController
{
    class DB
    {
        SqlConnection rConn;

        public DB(String serverURL, String databaseName)
        {
            this.rConn = new SqlConnection("Server=" + serverURL + ";Database=" + databaseName + ";Trusted_Connection=Yes;");
        }

        public void ConnectDb()
        {
            if (rConn.State != ConnectionState.Open)
                this.rConn.Open();
        }

        public void DisconnectDb()
        {
            try
            {
                this.rConn.Close();
            }
            catch (AmbiguousMatchException)
            {
            };
        }

        private static SqlDbType GetDbType(Object obj)
        {

            Type t = obj.GetType();


            if (t == typeof(int))
                return SqlDbType.Int;

            if (t == typeof(string))
                return SqlDbType.VarChar;

            if (t == typeof(long))
                return SqlDbType.BigInt;

            if (t == typeof(Boolean))
                return SqlDbType.TinyInt;

            if (t == typeof(float))
                return SqlDbType.Float;

            if (t == typeof(double))
                return SqlDbType.Float;

            if (t == typeof(object[]))
                return SqlDbType.Binary;

            throw new NotSupportedException("Variable type: " + t.ToString());
        }


        private static int GetDbSize(Object obj)
        {

            Type t = obj.GetType();

            if (t == typeof(int))
                return obj.ToString().Length;

            if (t == typeof(string))
                return obj.ToString().Length + 1;

            if (t == typeof(long))
                return obj.ToString().Length;

            if (t == typeof(Boolean))
                return 1;

            if (t == typeof(float))
                return obj.ToString().Length;

            if (t == typeof(double))
                return obj.ToString().Length;

            if (t == typeof(object[]))
                return ((object[])obj).Length;

            throw new NotSupportedException("Variable type: " + t.ToString());
        }


        private SqlCommand ParseStatement(String stm, params Object[] list)
        {

            SqlCommand command = new SqlCommand(null, this.rConn);
            SqlParameter param;

            var regex = new Regex(Regex.Escape("?"));

            int i = 0;

            while (stm.Contains("?"))
            {
                stm = regex.Replace(stm, "@var" + i, 1);
                param = new SqlParameter("@var" + i, GetDbType(list[i]), GetDbSize(list[i]));
                param.Value = list[i];
                command.Parameters.Add(param);
                i++;
            }

            if (list != null && i != list.Length)
                throw new System.ArgumentException("Parameters count error");

            command.CommandText = stm;

            return command;
        }


        protected SqlDataReader ExecSelect(String stm, params Object[] list)
        {
            ConnectDb();

            var cmd = this.ParseStatement(stm, list);

            cmd.Prepare();
            var res = cmd.ExecuteReader();

            if (!res.HasRows)
                return null;

            return res;
        }

        protected int ExecApply(String stm, params Object[] list)
        {
            ConnectDb();

            var cmd = this.ParseStatement(stm, list);
            cmd.Prepare();

            return cmd.ExecuteNonQuery();
        }

        protected string getAsString(object obj)
        {
            return ((string)obj).TrimEnd();
        }
        protected int getAsInt(object obj)
        {
            return (int)obj;
        }
        protected long getAsLong(object obj)
        {
            return (long)obj;
        }

    }

}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ThesesController
{
    class DBS : DB
    {

        public DBS(String serverURL, String databaseName)
            : base(serverURL, databaseName)
        {
        }


        public int InsertCountry(String country)
        {
            int res;

            res = this.ExecApply("insert into COUNTRIES (NAME) values (?)", country);

            return res;
        }

        public List<String> GetCountries()
        {
            List<String> res = new List<String>();
            var dataReader = this.ExecSelect("select * from COUNTRIES");

            while (dataReader.Read())
            {
                string tmp = this.getAsString(dataReader["NAME"]);

                res.Add(tmp);
            }

            return res;
        }

    }
}

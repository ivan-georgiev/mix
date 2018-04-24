using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class DEGREE
    {
        public DEGREE()
        {
            this.SPECIALITIES = new List<SPECIALITy>();
        }

        public int ID { get; set; }
        public string NAME { get; set; }
        public virtual ICollection<SPECIALITy> SPECIALITIES { get; set; }
    }
}

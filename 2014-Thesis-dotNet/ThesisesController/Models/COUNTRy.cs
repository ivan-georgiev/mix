using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class COUNTRy
    {
        public COUNTRy()
        {
            this.THESISES = new List<THESIS>();
        }

        public int ID { get; set; }
        public string NAME { get; set; }
        public virtual ICollection<THESIS> THESISES { get; set; }
    }
}

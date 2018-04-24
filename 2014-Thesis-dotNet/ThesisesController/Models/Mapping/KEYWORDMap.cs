using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class KEYWORDMap : EntityTypeConfiguration<KEYWORD>
    {
        public KEYWORDMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.WORD)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(20);

            // Table & Column Mappings
            this.ToTable("KEYWORDS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.WORD).HasColumnName("WORD");
        }
    }
}

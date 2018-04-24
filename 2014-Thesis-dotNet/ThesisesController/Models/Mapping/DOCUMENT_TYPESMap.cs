using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class DOCUMENT_TYPESMap : EntityTypeConfiguration<DOCUMENT_TYPES>
    {
        public DOCUMENT_TYPESMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.NAME)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(20);

            this.Property(t => t.DESCRIPTION)
                .IsFixedLength()
                .HasMaxLength(100);

            // Table & Column Mappings
            this.ToTable("DOCUMENT_TYPES");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.NAME).HasColumnName("NAME");
            this.Property(t => t.DESCRIPTION).HasColumnName("DESCRIPTION");
        }
    }
}

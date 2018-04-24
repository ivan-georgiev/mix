using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class PASSWORDMap : EntityTypeConfiguration<PASSWORD>
    {
        public PASSWORDMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.PASS)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(50);

            // Table & Column Mappings
            this.ToTable("PASSWORDS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.OFFICIAL_ID).HasColumnName("OFFICIAL_ID");
            this.Property(t => t.PASS).HasColumnName("PASS");

            // Relationships
            this.HasRequired(t => t.OFFICIAL)
                .WithMany(t => t.PASSWORDS)
                .HasForeignKey(d => d.OFFICIAL_ID);

        }
    }
}

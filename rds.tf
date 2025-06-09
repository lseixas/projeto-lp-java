#create our rds DB using TF

resource "aws_db_instance" "myrds" {
  engine              = "mysql"
  engine_version      = "8.0.41"
  allocated_storage   = 1
  storage_type        = "gp2"
  identifier          = "mydb"
  username            = "root"
  password            = "rootpass123"
  publicly_accessible = true
  skip_final_snapshot = true
  instance_class      = "db.t3.micro"
  db_name             = "aws_banco_cvetti"

}
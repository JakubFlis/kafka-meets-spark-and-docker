from kafka import KafkaProducer
from faker import Faker
import random
import time

fake = Faker()

isbn_numbers = list(open("isbns.txt"))

producer = KafkaProducer(bootstrap_servers='host.docker.internal:9092',         
                                          value_serializer=str.encode)

while 1 == 1:
    for _ in range(10):
        author = fake.name()
        text = fake.text()
        year = fake.year()
        title = fake.catch_phrase()
        publisher = fake.company()
        price = random.uniform(0.5, 20)
        isbn = random.choice(isbn_numbers).rstrip()

        result = '<?xml version="1.0" encoding="utf-8" ?><book year="%s"><title>%s</title><author>%s</author><publisher>%s</publisher><isbn>%s</isbn><price>%d</price></book>' % (year, title, author, publisher, isbn, price)
        print("Sendig: ")
        print(result)
        producer.send('foo', value=result)

    time.sleep(5)

producer.close()

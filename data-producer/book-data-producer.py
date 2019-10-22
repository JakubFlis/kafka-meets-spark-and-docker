#!/usr/bin/python
import sys, getopt, time, random
from kafka import KafkaProducer
from faker import Faker

class BookDataProducer:
    """
    A class used to generate and send fake XML data to given Kafka topic.
    """

    fake = Faker()
    
    def __init__(self, isbn_txt_file_path, kafka_topic, batch_size=10, batch_delay=5, bootstrap_server='host.docker.internal:9092'):
        """
        Parameters
        ----------
        isbn_txt_file_path : str
            Path to a text file containing ISBN numbers (separated by \n)
        kafka_topic : str
            A Kafka topic that fake XMLs will be send to
        batch_size : int, optional
            Number of messages sent to Kafka continously without delays
        batch_delay : int, optional
            Delay between messages batches (in seconds)
        bootstrap_server : string, optional
            Kafka bootstrap server (host:port)
        """
        BATCH_SIZE = batch_size
        SECONDS_PER_BATCH = batch_delay
        kafka_topic = kafka_topic
        isbn_numbers = list(open(isbn_txt_file_path))
        kafka_producer = KafkaProducer(bootstrap_servers=bootstrap_server,         
                                          value_serializer=str.encode)

    def start_producing_kafka_messages(self):
        """
            Loads generated XML into Kafka.
            Runs in infinite mode in BATCH_SIZE batches with SECONDS_PER_BATCH delay for each batch. 
        """
        while True:
            for _ in range(self.BATCH_SIZE):
                fake_xml = self.produce_fake_xml()
                print(fake_xml)
                self.kafka_producer.send(self.kafka_topic, value=fake_xml)
            time.sleep(self.SECONDS_PER_BATCH)

    def get_random_isbn(self):
        """
            Gets a random ISBN number from previously loaded text file.
        """
        return random.choice(self.isbn_numbers).rstrip()

    def produce_fake_xml(self):
        """
            Prepares fake values Book and encapsulates it in a form of XML structure.
        """
        author = fake.name()
        text = fake.text()
        year = fake.year()
        title = fake.catch_phrase()
        publisher = fake.company()
        price = random.uniform(0.5, 20)
        isbn = get_random_isbn()
        return '<?xml version="1.0" encoding="utf-8" ?><book year="%s"><title>%s</title><author>%s</author><publisher>%s</publisher><isbn>%s</isbn><price>%d</price></book>' % (year, title, author, publisher, isbn, price)


def main(argv):
    isbn_path = ''
    kafka_topic = ''
    batch_size = None
    batch_delay = None
    bootstrap_server = ''
    try:
        opts, args = getopt.getopt(argv, "hi:k:s:d:b:", ["isbn=", "kafka_topic=", "batch_size=", "batch_delay=", "bootstrap_server="])
    except getopt.GetoptError as err:
        print(err)
        sys.exit(2)
    for opt, arg in opts:
        if opt in ("-i", "--isbn"):
            isbn_path = arg
        elif opt in ("-k", "--kafka_topic"):
            kafka_topic = arg
        elif opt in ("-s", "--batch_size"):
            batch_size = arg
        elif opt in ("-d", "--batch_delay"):
            batch_delay = arg
        elif opt in ("-b", "--bootstrap_server"):
            bootstrap_server = arg

        data_producer = BookDataProducer(isbn_path, kafka_topic, batch_size, batch_delay, bootstrap_server) 
        data_producer.start_producing_kafka_messages()

if __name__ == "__main__":
    main(sys.argv[1:])
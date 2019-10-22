FROM python:3

ADD book-data-producer.py /
ADD isbns.txt /

RUN pip install kafka-python
RUN pip install Faker

CMD [ "python", "./book-data-producer.py -i isbns.txt -k foo -s 10 -d 5 -b host.docker.internal:9092" ]
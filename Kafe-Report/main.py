from botocore.client import Config
import matplotlib.pyplot as plt
import json
import pika
import boto3
import io
import os
import uuid


ACCESS_KEY_ID = os.getenv("ACCESS_KEY_ID")
ACCESS_SECRET_KEY = os.getenv("ACCESS_SECRET_KEY")
BUCKET_NAME = os.getenv("BUCKET_NAME")
REGION = os.getenv("REGION")
URL = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/"

S3 = boto3.resource('s3', aws_access_key_id=ACCESS_KEY_ID, aws_secret_access_key=ACCESS_SECRET_KEY,
                    config=Config(signature_version='s3v4'))


def generate_figure():
    fig, ax = plt.subplots()
    width = 768 / fig.dpi
    height = 576 / fig.dpi
    fig.set_figwidth(width)
    fig.set_figheight(height)
    fig.set_facecolor('white')
    # ax.axes.get_xaxis().set_visible(False)
    # ax.axes.get_yaxis().set_visible(False)
    return fig, ax


def generate_pie_image(statistics):
    fig, ax = generate_figure()
    keys = list()
    values = list()
    for pair in statistics:
        keys.append(pair[0])
        values.append(pair[1])
    pie = ax.pie(
        values,
        autopct=lambda p: '{:.0f}'.format(p * sum(values) / 100),
        counterclock=False,
        startangle=90
    )
    pos = ax.get_position()
    ax.set_position([pos.x0, pos.y0, pos.width, pos.height * 0.85])
    ax.legend(pie[0], keys, loc='upper center', bbox_to_anchor=(0.5, 1.35), ncol=3)
    browser_buffer = io.BytesIO()
    plt.savefig(browser_buffer, format='png')
    browser_buffer.seek(0)
    plt.close('all')
    plt.clf()
    return browser_buffer


class ReportCreationConsumer:
    def __init__(self):
        self.publishChannel = "TEST"
        self.__url = os.getenv("MQ_URL")
        self.__port = os.getenv("MQ_PORT")
        self.__vhost = os.getenv("MQ_VHOST")
        self.__cred = pika.PlainCredentials(os.getenv('MQ_USER'), os.getenv('MQ_PASSWORD'))
        self.__report_queue = os.getenv('MQ_REPORT_QUEUE')
        self.__mail_queue = os.getenv("MQ_MAIL_QUEUE")
        return

    def consume(self, method_frame, header_frame, body):
        days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]
        data = json.loads(body.decode("utf-8"))
        name = data.get("name")
        email = data.get("email")
        browser_statistic = sorted(data.get("browser").items(), key=lambda x: x[1], reverse=True)
        os_statistics = sorted(data.get("os").items(), key=lambda x: x[1], reverse=True)
        sns_statistics = sorted(data.get("sns").items(), key=lambda x: x[1], reverse=True)
        day_of_week_statistics = data.get("dayOfWeek")
        for day in days:
            if day not in day_of_week_statistics:
                day_of_week_statistics[day] = 0

        browser_image = generate_pie_image(browser_statistic)
        os_image = generate_pie_image(os_statistics)
        sns_image = generate_pie_image(sns_statistics)

        fig, ax = generate_figure()
        keys = list()
        values = list()
        for day in days:
            keys.append(day)
            values.append(day_of_week_statistics.get(day))
        bar = ax.bar(keys, values)
        day_image = io.BytesIO()
        plt.savefig(day_image, format='png')
        day_image.seek(0)
        plt.close('all')
        plt.clf()

        file_name = str(uuid.uuid4())
        S3.Bucket(BUCKET_NAME).put_object(Key=(file_name + "_0.png"), Body=browser_image, ContentType='image/png')
        S3.Bucket(BUCKET_NAME).put_object(Key=(file_name + "_1.png"), Body=os_image, ContentType='image/png')
        S3.Bucket(BUCKET_NAME).put_object(Key=(file_name + "_2.png"), Body=sns_image, ContentType='image/png')
        S3.Bucket(BUCKET_NAME).put_object(Key=(file_name + "_3.png"), Body=day_image, ContentType='image/png')
        browser_image.close()
        os_image.close()
        sns_image.close()
        day_image.close()

        mail_template = dict()
        mail_template["name"] = name
        mail_template["email"] = email
        mail_template["browserData"] = URL + file_name + "_0.png"
        mail_template["osData"] = URL + file_name + "_1.png"
        mail_template["snsData"] = URL + file_name + "_2.png"
        mail_template["dayOfWeekData"] = URL + file_name + "_3.png"

        mail_data = json.dumps(mail_template)

        connection = self.connection
        publish_channel = connection.channel()
        publish_channel.basic_publish(
            exchange="chart.exchange",
            routing_key="chart.key",
            body=mail_data
        )
        publish_channel.close()

    def run(self):
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(self.__url, self.__port, self.__vhost, self.__cred))
        print(type(connection))
        consume_channel = connection.channel()
        consume_channel.exchange_declare(exchange='chart.exchange', exchange_type='direct')
        consume_channel.queue_declare(queue='chart.queue')
        consume_channel.queue_bind(exchange="chart.exchange", queue="chart.queue", routing_key="chart.key")
        consume_channel.basic_consume(
            queue=self.__report_queue,
            on_message_callback=ReportCreationConsumer.consume,
            auto_ack=True
        )
        print("Start Consume Report Creation Request...")
        consume_channel.start_consuming()
        return


def main():
    consumer = ReportCreationConsumer()
    try:
        consumer.run()
    except KeyboardInterrupt:
        print("Receive Keyboard Interrupt, Close Process....")


if __name__ == '__main__':
    main()

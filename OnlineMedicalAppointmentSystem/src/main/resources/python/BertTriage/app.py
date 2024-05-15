# -*- coding:utf-8 -*-
import sys
import io
import tensorflow as tf 
from keras.backend.tensorflow_backend import set_session
from bert4keras.tokenizers import Tokenizer
from bert4keras.snippets import sequence_padding

from bert_model import build_bert_model

global graph,model,sess 


config = tf.ConfigProto()
config.gpu_options.allow_growth=True
sess = tf.Session(config=config)
graph = tf.get_default_graph()
set_session(sess)

class BertTraigeModel(object):
    def __init__(self):
        super(BertTraigeModel, self).__init__()
        self.dict_path = './bert_weight_files/roberta/vocab.txt'
        self.config_path='./bert_weight_files/roberta/bert_config_rbt3.json'
        self.checkpoint_path='./bert_weight_files/roberta/bert_model.ckpt'

        self.label_list = [line.strip() for line in open('./data/label','r',encoding='utf8')]
        self.id2label = {idx:label for idx,label in enumerate(self.label_list)}

        self.tokenizer = Tokenizer(self.dict_path)
        self.model = build_bert_model(self.config_path,self.checkpoint_path,13)
        self.model.load_weights('./checkpoint/best_model.weights')

    def predict(self,text):
        token_ids, segment_ids = self.tokenizer.encode(text, maxlen=60)
        proba = self.model.predict([[token_ids], [segment_ids]])
        rst = {l:p for l,p in zip(self.label_list,proba[0])}
        rst = sorted(rst.items(), key = lambda kv:kv[1],reverse=True)
        name,confidence = rst[0]
        # return {"name":name,"confidence":float(confidence)}
        return name


if __name__ == '__main__':
    sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
    BTM = BertTraigeModel()
    print("Python process ready")
    while True:
        text = input()
        # text = "您好专家，我一个朋友检查出是鼻咽癌，看看他没有任何症状，我们都很担心，想问下鼻咽癌早期有什么症状？鼻咽癌应该如何治疗好呢？"
        print(BTM.predict(text))
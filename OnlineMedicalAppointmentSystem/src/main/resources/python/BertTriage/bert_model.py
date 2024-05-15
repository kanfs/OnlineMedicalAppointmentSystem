from bert4keras.backend import keras, set_gelu
from bert4keras.models import build_transformer_model

set_gelu("tanh")

def textcnn(inputs, kernel_initializer): # 用一维卷积提取序列特征
    cnn1 = keras.layers.Conv1D(
			256,
			3,
			strides=1,
			padding='same',
			activation='relu',
			kernel_initializer=kernel_initializer
		)(inputs) # shape=[batch_size,maxlen-2,256]
    cnn1 = keras.layers.GlobalMaxPooling1D()(cnn1)  # shape=[batch_size,256]

    cnn2 = keras.layers.Conv1D(
			256,
			4,
			strides=1,
			padding='same',
			activation='relu',
			kernel_initializer=kernel_initializer
		)(inputs)
    cnn2 = keras.layers.GlobalMaxPooling1D()(cnn2)

    cnn3 = keras.layers.Conv1D(
			256,
			5,
			strides=1,
			padding='same',
			kernel_initializer=kernel_initializer
		)(inputs)
    cnn3 = keras.layers.GlobalMaxPooling1D()(cnn3)

    output = keras.layers.concatenate(
		[cnn1,cnn2,cnn3],
		axis=-1)
    output = keras.layers.Dropout(0.2)(output)
    return output

def build_bert_model(config_path, checkpoint_path, class_nums):
    bert = build_transformer_model(
        config_path = config_path,
        checkpoint_path = checkpoint_path,
        model="bert",
        return_keras_model=False,
    )
    # 使用Lambda层将BERT模型的输出（bert.model.output）的第一个元素（x[:,0]）提取出来，
	# 然后将其命名为cls-token，最后将提取出的特征作为输出。
    cls_features = keras.layers.Lambda(
		lambda x:x[:,0],
		name='cls-token'
		)(bert.model.output) #shape=[batch_size,768] 
	# all_token_embedding为去掉[CLS]和[SEP]的有意义token组成的embedding矩阵
    all_token_embedding = keras.layers.Lambda(
		lambda x:x[:,1:-1],
		name='all-token'
		)(bert.model.output) #shape=[batch_size,maxlen-2,768]

    cnn_features = textcnn(all_token_embedding,bert.initializer) #shape=[batch_size,cnn_output_dim]
	
    concat_features = keras.layers.concatenate([cls_features,cnn_features],axis=-1)

	# 两个全连接层
    dense = keras.layers.Dense(
			units=512,
			activation='relu',
			kernel_initializer=bert.initializer
		)(concat_features)
 
    output = keras.layers.Dense(
			units=class_nums,
			activation='softmax',
			kernel_initializer=bert.initializer
		)(dense)

    model = keras.models.Model(bert.model.input,output)

    return model

if __name__ == '__main__':
	config_path='./bert_weight_files/bert_wwm/bert_config.json'
	checkpoint_path='./bert_weight_files/bert_wwm/bert_model.ckpt'
	class_nums=13
	build_bert_model(config_path, checkpoint_path, class_nums)